JDBC Practice

This repository contains three beginner–friendly JDBC + SQL practice projects to learn how
 to connect Java with relational databases (MySQL).
Each project demonstrates table creation, SQL queries, and JDBC code for CRUD operations and transactions.

📂 Project Structure
1️⃣ Employee–Department Management

SQL
Tables: Employee(emp_id, name, designation, salary, dept_id), Department(dept_id, dept_name)
Queries:
Fetch employees by department
Find highest-paid employee in each department
Update manager salaries by 10%
Delete employees with salary < 30,000

JDBC Features
Connect to DB
Insert new employee (user input)
Fetch employees by department name
Update salary of an employee
Delete an employee by ID


2️⃣ Online Order System

SQL
Tables: Customer(cust_id, cust_name, email), Orders(order_id, cust_id, order_date, amount)
Queries:
Orders by a specific customer
Customer with highest total purchase
Customers with no orders
Average order value

JDBC Features
Insert new order
Fetch customer’s order history
Show top 3 customers by purchase amount
Transaction handling: insert into Orders + update customer’s last order date atomically


3️⃣ Library Management


SQL
Tables: Book(book_id, title, author, available_copies),
Student(student_id, name, branch),
Issue(issue_id, student_id, book_id, issue_date, return_date)
Queries:
Books issued by a student
Most issued book
Check availability by title
Branch-wise issued book count

JDBC Features
Issue book (decrement available copies)
Return book (increment available copies + set return date)
Search books by title/author
Student-wise issue history
