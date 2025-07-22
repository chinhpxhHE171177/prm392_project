// models/employeeModel.js
const db = require('../config/db.js');

class Employee {
    // Create new employee
    static async create(employeeData) {
        try {
            const [result] = await db.execute(
                `INSERT INTO employees 
                (user_id, employee_code, first_name, last_name, email, phone, department_id, position, hire_date) 
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)`,
                [
                    employeeData.user_id,
                    employeeData.employee_code,
                    employeeData.first_name,
                    employeeData.last_name,
                    employeeData.email,
                    employeeData.phone,
                    employeeData.department_id,
                    employeeData.position,
                    employeeData.hire_date
                ]
            );

            return result.insertId ? this.findById(result.insertId) : null;
        } catch (error) {
            console.error('Error creating employee:', error);
            throw new Error('Failed to create employee');
        }
    }

    // Get all employees with pagination, search, filter
    static async getAll({ page = 1, limit = 10, search = '', filter = '' }) {
        try {
            const offset = (page - 1) * limit;
            let query = `
                SELECT 
                    e.id, e.user_id, e.employee_code, e.first_name, e.last_name, 
                    e.email, e.phone, e.department_id, e.position, e.hire_date, e.status
                FROM employees e
                JOIN users u ON e.user_id = u.id
                WHERE u.role IN ('employee', 'dep_manager', 'hr')
                AND e.status = 'active'
                AND u.is_active = TRUE
            `;
            const values = [];

            if (search) {
                query += ` AND (
                    e.first_name LIKE ? OR e.last_name LIKE ? OR 
                    e.employee_code LIKE ? OR e.email LIKE ?
                )`;
                const term = `%${search}%`;
                values.push(term, term, term, term);
            }

            if (filter) {
                query += ` AND e.position = ?`;
                values.push(filter);
            }

            query += ` ORDER BY e.id DESC LIMIT ? OFFSET ?`;
            values.push(parseInt(limit), parseInt(offset));

            const [rows] = await db.execute(query, values);

            // Count total
            let countQuery = `
                SELECT COUNT(*) AS total 
                FROM employees e
                JOIN users u ON e.user_id = u.id
                WHERE u.role IN ('employee', 'dep_manager', 'hr')
                AND e.status = 'active'
                AND u.is_active = TRUE
            `;
            const countValues = [];

            if (search) {
                countQuery += ` AND (
                    e.first_name LIKE ? OR e.last_name LIKE ? OR 
                    e.employee_code LIKE ? OR e.email LIKE ?
                )`;
                const term = `%${search}%`;
                countValues.push(term, term, term, term);
            }

            if (filter) {
                countQuery += ` AND e.position = ?`;
                countValues.push(filter);
            }

            const [[{ total }]] = await db.execute(countQuery, countValues);

            return {
                employees: rows,
                total,
                page: parseInt(page),
                limit: parseInt(limit)
            };
        } catch (error) {
            throw new Error(`Error fetching employees: ${error.message}`);
        }
    }

    // Find by ID
    static async findById(id) {
        try {
            const [rows] = await db.execute(
                `SELECT * FROM employees WHERE id = ?`,
                [id]
            );
            return rows[0] || null;
        } catch (error) {
            throw new Error(`Error finding employee: ${error.message}`);
        }
    }

    // Update
    static async update(id, data) {
        try {
            const fields = [];
            const values = [];

            for (const key in data) {
                fields.push(`${key} = ?`);
                values.push(data[key]);
            }

            if (fields.length === 0) return false;

            values.push(id);
            const query = `UPDATE employees SET ${fields.join(', ')} WHERE id = ?`;

            const [result] = await db.execute(query, values);
            return result.affectedRows > 0 ? this.findById(id) : null;
        } catch (error) {
            throw new Error(`Error updating employee: ${error.message}`);
        }
    }

    // Delete
    static async delete(id) {
        try {
            const [result] = await db.execute('DELETE FROM employees WHERE id = ?', [id]);
            return result.affectedRows > 0;
        } catch (error) {
            throw new Error(`Error deleting employee: ${error.message}`);
        }
    }
}

module.exports = Employee;
