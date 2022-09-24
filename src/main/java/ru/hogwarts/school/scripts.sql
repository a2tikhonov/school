select * from student;

select * from student where age >= 20 and age <= 40;

select name from student;

select * from student where name like '%O%' or name like '%o%';

select * from student where student.age < student.id;

select * from student order by student.age;

select * from student, faculty where student.faculty_id = faculty.id;