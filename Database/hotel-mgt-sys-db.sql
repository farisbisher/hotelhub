create table Employee (Fname VARCHAR(255), Lname VARCHAR(255), salary INT, EmployeeID INT(10) PRIMARY KEY, isAdmin BOOLEAN DEFAULT FALSE);
create table Customer (Fname varchar(255), Lname varchar(255), PhoneNo int(10), CustomerID int(10) primary key);
create table Room (RoomNo int(3) primary key, NoRooms int(1), Price int, Floor int(3), RoomStatus varchar(255));
create table Checkout (ID int primary key auto_increment ,SerialNo varchar(255), RoomNo int(3), EmployeeID int(10), CustomerID int(10), foreign key (SerialNo) references reservation(SerialNo), foreign key (RoomNo) references Room(RoomNo), foreign key (EmployeeID) references Employee(EmployeeID), foreign key (CustomerID) references Customer(CustomerID));
create table Reservation (sDate Date, eDate Date, totalPrice int, RoomNo int, CustomerID int(10), SerialNo varchar(255) primary key, membersNo int, EmployeeID int(10), isPaid boolean, foreign key (RoomNo) references Room(RoomNo), foreign key (EmployeeID) references Employee(EmployeeID), foreign key (CustomerID) references Customer(CustomerID));
INSERT INTO Employee (Fname, Lname, salary, EmployeeID, isAdmin)
VALUES ('Saad', 'Alhussain', 20000, 4211494, true);
