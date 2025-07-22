// attendanceModel.js
const db = require('../config/db.js');
const moment = require('moment-timezone');

class Attendance {
    // Check if attendance exists for employee and date
    static async checkExisting(employee_id, date) {
        try {
            const formattedDate = moment.utc(date).format('YYYY-MM-DD');
            const [rows] = await db.execute(
                'SELECT id FROM attendance WHERE employee_id = ? AND date = ?',
                [employee_id, formattedDate]
            );
            return rows[0] || null;
        } catch (error) {
            throw new Error(`Không thể kiểm tra bản ghi chấm công: ${error.message}`);
        }
    }

    // Create a new attendance record
    static async create(attendanceData) {
        try {
            const {
                employee_id,
                date,
                check_in_time,
                check_out_time,
                status = 'present',
                notes
            } = attendanceData;

            // Validate required fields
            if (!employee_id || !date || !check_in_time) {
                throw new Error('Yêu cầu ID nhân viên, ngày và thời gian check-in');
            }
            if (!['present', 'absent', 'late', 'early_leave'].includes(status)) {
                throw new Error('Giá trị trạng thái không hợp lệ');
            }

            // Check for existing record
            const existing = await this.checkExisting(employee_id, date);
            if (existing) {
                throw new Error('Bản ghi chấm công đã tồn tại cho nhân viên này trong ngày này');
            }

            const query = `
                INSERT INTO attendance (employee_id, date, check_in_time, check_out_time, status, notes)
                VALUES (?, ?, ?, ?, ?, ?)
            `;
            const values = [
                employee_id,
                moment.utc(date).format('YYYY-MM-DD'),
                moment.utc(check_in_time).format('YYYY-MM-DD HH:mm:ss'),
                check_out_time ? moment.utc(check_out_time).format('YYYY-MM-DD HH:mm:ss') : null,
                status,
                notes || null
            ];
            //console.log('Creating attendance with values:', values);
            const [result] = await db.execute(query, values);

            return result.insertId ? await this.findById(result.insertId) : null;
        } catch (error) {
            throw new Error(`Không thể tạo bản ghi chấm công: ${error.message}`);
        }
    }

    // Find attendance by ID
    static async findById(id) {
        try {
            const [rows] = await db.execute(`
                SELECT a.*, 
                       CONCAT(e.first_name, ' ', e.last_name) as employee_name,
                       d.name as department_name
                FROM attendance a
                LEFT JOIN employees e ON a.employee_id = e.id
                LEFT JOIN departments d ON e.department_id = d.id
                WHERE a.id = ?
            `, [id]);
            return rows[0] || null;
        } catch (error) {
            throw new Error('Không thể tìm bản ghi chấm công');
        }
    }

    // Get attendance records with filters
    // static async getAll({ employee_id, start_date, end_date, status, department_id, page = 1, limit = 10 }) {
    //     try {
    //         const offset = (page - 1) * limit;
    //         let query = `
    //             SELECT a.*, 
    //                    CONCAT(e.first_name, ' ', e.last_name) as employee_name,
    //                    d.name as department_name
    //             FROM attendance a
    //             LEFT JOIN employees e ON a.employee_id = e.id
    //             LEFT JOIN departments d ON e.department_id = d.id
    //             WHERE 1=1
    //         `;
    //         const values = [];

    //         if (employee_id) {
    //             query += ' AND a.employee_id = ?';
    //             values.push(employee_id);
    //         }
    //         if (start_date) {
    //             query += ' AND a.date >= ?';
    //             values.push(moment.utc(start_date).format('YYYY-MM-DD'));
    //         }
    //         if (end_date) {
    //             query += ' AND a.date <= ?';
    //             values.push(moment.utc(end_date).format('YYYY-MM-DD'));
    //         }
    //         if (status) {
    //             query += ' AND a.status = ?';
    //             values.push(status);
    //         }
    //         if (department_id) {
    //             query += ' AND e.department_id = ?';
    //             values.push(department_id);
    //         }

    //         query += ' ORDER BY a.date DESC LIMIT ? OFFSET ?';
    //         values.push(limit, offset);

    //         const [rows] = await db.execute(query, values);

    //         const [countResult] = await db.execute(`
    //             SELECT COUNT(*) as total
    //             FROM attendance a
    //             LEFT JOIN employees e ON a.employee_id = e.id
    //             WHERE 1=1
    //             ${employee_id ? 'AND a.employee_id = ?' : ''}
    //             ${start_date ? 'AND a.date >= ?' : ''}
    //             ${end_date ? 'AND a.date <= ?' : ''}
    //             ${status ? 'AND a.status = ?' : ''}
    //             ${department_id ? 'AND e.department_id = ?' : ''}
    //         `, values.slice(0, -2));

    //         return {
    //             records: rows,
    //             total: countResult[0].total,
    //             page,
    //             limit
    //         };
    //     } catch (error) {
    //         throw new Error('Không thể lấy danh sách chấm công');
    //     }
    // }
    static async getAll({ employee_id, date, start_date, end_date, status, department_id, page = 1, limit = 10 }) {
        try {
            console.log('Input params:', { employee_id, date, start_date, end_date, status, department_id, page, limit });
            const offset = (page - 1) * limit;
            let query = `
                SELECT a.*, 
                       CONCAT(e.first_name, ' ', e.last_name) as employee_name,
                       d.name as department_name,
                       e.email
                FROM attendance a
                LEFT JOIN employees e ON a.employee_id = e.id
                LEFT JOIN departments d ON e.department_id = d.id
                WHERE 1=1
            `;
            const values = [];

            if (employee_id) {
                query += ' AND a.employee_id = ?';
                values.push(employee_id);
            }
            if (date) {
                console.log('Processing date:', date);
                if (moment(date, 'YYYY-MM-DD', true).isValid()) {
                    query += ' AND DATE(a.date) = ?';
                    values.push(moment.utc(date).startOf('day').format('YYYY-MM-DD'));
                } else {
                    console.warn(`Invalid date format: ${date}`);
                }
            }
            if (start_date) {
                console.log('Processing start_date:', start_date);
                if (moment(start_date, 'YYYY-MM-DD', true).isValid()) {
                    query += ' AND DATE(a.date) >= ?';
                    values.push(moment.utc(start_date).startOf('day').format('YYYY-MM-DD'));
                } else {
                    console.warn(`Invalid start_date format: ${start_date}`);
                }
            }
            if (end_date) {
                console.log('Processing end_date:', end_date);
                if (moment(end_date, 'YYYY-MM-DD', true).isValid()) {
                    query += ' AND DATE(a.date) <= ?';
                    values.push(moment.utc(end_date).endOf('day').format('YYYY-MM-DD'));
                } else {
                    console.warn(`Invalid end_date format: ${end_date}`);
                }
            }
            if (status) {
                query += ' AND a.status = ?';
                values.push(status);
            }
            if (department_id) {
                query += ' AND e.department_id = ?';
                values.push(department_id);
            }

            query += ' ORDER BY a.date DESC LIMIT ? OFFSET ?';
            values.push(limit, offset);

            console.log('Executing query:', query, 'with values:', values);
            const [rows] = await db.execute(query, values);

            let countQuery = `
                SELECT COUNT(*) as total
                FROM attendance a
                LEFT JOIN employees e ON a.employee_id = e.id
                LEFT JOIN departments d ON e.department_id = d.id
                WHERE 1=1
            `;
            const countValues = [];

            if (employee_id) {
                countQuery += ' AND a.employee_id = ?';
                countValues.push(employee_id);
            }
            if (date) {
                if (moment(date, 'YYYY-MM-DD', true).isValid()) {
                    countQuery += ' AND DATE(a.date) = ?';
                    countValues.push(moment.utc(date).startOf('day').format('YYYY-MM-DD'));
                }
            }
            if (start_date) {
                if (moment(start_date, 'YYYY-MM-DD', true).isValid()) {
                    countQuery += ' AND DATE(a.date) >= ?';
                    countValues.push(moment.utc(start_date).startOf('day').format('YYYY-MM-DD'));
                }
            }
            if (end_date) {
                if (moment(end_date, 'YYYY-MM-DD', true).isValid()) {
                    countQuery += ' AND DATE(a.date) <= ?';
                    countValues.push(moment.utc(end_date).endOf('day').format('YYYY-MM-DD'));
                }
            }
            if (status) {
                countQuery += ' AND a.status = ?';
                countValues.push(status);
            }
            if (department_id) {
                countQuery += ' AND e.department_id = ?';
                countValues.push(department_id);
            }

            console.log('Executing countQuery:', countQuery, 'with countValues:', countValues);
            const [countResult] = await db.execute(countQuery, countValues);

            return {
                records: rows,
                total: countResult[0].total,
                page,
                limit
            };
        } catch (error) {
            console.error('Error in getAll:', error);
            throw new Error('Không thể lấy danh sách chấm công: ' + error.message);
        }
    }


    // Update an attendance record
    static async update(id, attendanceData) {
        try {
            const { check_in_time, check_out_time, status, notes } = attendanceData;

            const fields = [];
            const values = [];

            if (check_in_time) {
                fields.push('check_in_time = ?');
                values.push(moment.utc(check_in_time).format('YYYY-MM-DD HH:mm:ss'));
            }
            if (check_out_time !== undefined) {
                fields.push('check_out_time = ?');
                values.push(check_out_time ? moment.utc(check_out_time).format('YYYY-MM-DD HH:mm:ss') : null);
            }
            if (status) {
                if (!['present', 'absent', 'late', 'early_leave'].includes(status)) {
                    throw new Error('Giá trị trạng thái không hợp lệ');
                }
                fields.push('status = ?');
                values.push(status);
            }
            if (notes !== undefined) {
                fields.push('notes = ?');
                values.push(notes || null);
            }

            if (fields.length === 0) {
                throw new Error('Không có trường nào để cập nhật');
            }

            values.push(id);
            //console.log('Updating attendance with values:', values);
            const query = `UPDATE attendance SET ${fields.join(', ')} WHERE id = ?`;
            const [result] = await db.execute(query, values);

            return result.affectedRows > 0;
        } catch (error) {
            throw new Error(`Không thể cập nhật chấm công: ${error.message}`);
        }
    }

    // Get schedule for attendance
    static async getSchedule(employee_id, date) {
        try {
            const formattedDate = moment.utc(date).format('YYYY-MM-DD');
            const [rows] = await db.execute(`
                SELECT * FROM schedules
                WHERE employee_id = ? 
                AND date = ?
                AND status = 'confirmed'
            `, [employee_id, formattedDate]);
            return rows[0] || null;
        } catch (error) {
            throw new Error('Không thể lấy lịch làm việc');
        }
    }

    // Create a notification
    static async createNotification(user_id, title, message, type) {
        try {
            const query = `
                INSERT INTO notifications (user_id, title, message, type)
                VALUES (?, ?, ?, ?)
            `;
            await db.execute(query, [user_id, title, message, type]);
        } catch (error) {
            throw new Error(`Không thể tạo thông báo: ${error.message}`);
        }
    }
}

module.exports = Attendance;