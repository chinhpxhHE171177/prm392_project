const bcrypt = require('bcryptjs');

const passwords = [
  '1234567@@',
  '12345678@@',
  '123456789@@',
  '12345@@',
  '1234@@'
];

passwords.forEach(plainPassword => {
  bcrypt.hash(plainPassword, 10, (err, hash) => {
    if (err) {
      console.error(`Error hashing ${plainPassword}:`, err);
      return;
    }
    console.log('Password:', plainPassword);
    console.log('Bcrypt hash:', hash);
  });
});