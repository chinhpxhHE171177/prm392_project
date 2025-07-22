const express = require('express');
const router = express.Router();
const { createAttendance, getAttendance, updateAttendance, getAttendanceReport, checkIn, checkOut } = require('../controllers/attendanceController');
const { createAttendanceValidator, updateAttendanceValidator, checkInOutValidator } = require('../middleware/attendanceMiddleware'); // Thêm checkInOutValidator
const { verifyToken, checkRole } = require('../middleware/authMiddleware');

// Route hiện có
router.post('/', createAttendanceValidator, verifyToken, createAttendance);
router.get('/', verifyToken, getAttendance);
router.get('/report', verifyToken, checkRole('admin', 'hr', 'dep_manager'), getAttendanceReport);
router.put('/:id', updateAttendanceValidator, verifyToken, checkRole('admin', 'hr', 'dep_manager'), updateAttendance);

// Route mới cho check-in và check-out
router.post('/checkin', checkInOutValidator, verifyToken, checkIn);
router.post('/checkout', checkInOutValidator, verifyToken, checkOut);

module.exports = router;