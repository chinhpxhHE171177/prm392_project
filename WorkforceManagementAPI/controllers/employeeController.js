// controllers/employeeController.js
const Employee = require('../models/employeeModel');
const db = require('../config/db.js');

// Create
exports.createEmployee = async (req, res) => {
    try {
        const { user_id, employee_code, first_name, last_name, email, phone, department_id, position, hire_date } = req.body;

        if (!user_id || !employee_code || !first_name || !last_name || !email || !phone || !position || !hire_date) {
            return res.status(400).json({ success: false, message: 'All fields are required' });
        }

        const employee = await Employee.create({
            user_id, employee_code, first_name, last_name,
            email, phone, department_id, position, hire_date
        });

        return res.status(201).json({
            success: true,
            message: 'Employee created successfully',
            data: employee
        });
    } catch (error) {
        console.error('Create employee error:', error);
        return res.status(500).json({ success: false, message: error.message });
    }
};

// Get All
exports.getAllEmployees = async (req, res) => {
    try {
        const { page, limit, search, filter } = req.query;
        const result = await Employee.getAll({
            page: parseInt(page) || 1,
            limit: parseInt(limit) || 10,
            search: search || '',
            filter: filter || ''
        });

        return res.status(200).json({
            success: true,
            data: {
                employees: result.employees,
                total: result.total,
                page: result.page,
                limit: result.limit
            },
            message: 'Employees fetched successfully'
        });
    } catch (error) {
        console.error('Get all employees error:', error);
        return res.status(500).json({ success: false, message: error.message });
    }
};

// Get by ID
exports.getEmployeeById = async (req, res) => {
    try {
        const employee = await Employee.findById(req.params.id);
        if (!employee) {
            return res.status(404).json({ success: false, message: 'Employee not found' });
        }

        return res.status(200).json({ success: true, data: employee });
    } catch (error) {
        return res.status(500).json({ success: false, message: error.message });
    }
};

// Update
exports.updateEmployee = async (req, res) => {
    try {
        const updatedEmployee = await Employee.update(req.params.id, req.body);
        if (!updatedEmployee) {
            return res.status(400).json({ success: false, message: 'Failed to update employee' });
        }

        return res.status(200).json({
            success: true,
            message: 'Employee updated successfully',
            data: updatedEmployee
        });
    } catch (error) {
        return res.status(500).json({ success: false, message: error.message });
    }
};

// Delete
exports.deleteEmployee = async (req, res) => {
    try {
        const exists = await Employee.findById(req.params.id);
        if (!exists) {
            return res.status(404).json({ success: false, message: 'Employee not found' });
        }

        await Employee.delete(req.params.id);
        return res.status(200).json({ success: true, message: 'Employee deleted successfully' });
    } catch (error) {
        return res.status(500).json({ success: false, message: error.message });
    }
};
exports.getUsersWithoutEmployee = async (req, res) => {
    try {
        const [rows] = await db.execute(
            `SELECT u.id, u.username, u.email, u.first_name, u.last_name
             FROM users u
             LEFT JOIN employees e ON u.id = e.user_id

             WHERE u.role IN ('employee', 'dep_manager', 'hr')
             AND u.is_active = TRUE
             AND e.user_id IS NULL`
        );

        return res.status(200).json({
            success: true,
            data: rows,
            message: 'Users without employee records fetched successfully'
        });
    } catch (error) {
        console.error('Get users without employee error:', error);
        return res.status(500).json({ success: false, message: error.message });
    }
};