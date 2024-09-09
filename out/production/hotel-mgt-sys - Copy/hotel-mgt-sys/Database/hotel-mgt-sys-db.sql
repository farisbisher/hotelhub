create table Employee (Fname varchar(255), Lname varchar(255), salary float(5,2), EmployeeID int(10) primary key);
create table Customer (Fname varchar(255), Lname varchar(255), PhoneNo int(10), CustomerID int(10) primary key);
create table Room (RoomNo int(3) primary key, NoRooms int(1), Price float(4,2), Floor int(1), RoomStatus varchar(255));
create table Checkout (SerialNo int(10) primary key, RoomNo int(3), EmployeeID int(10), CustomerID int(10), foreign key (RoomNo) references Room(RoomNo), foreign key (EmployeeID) references Employee(EmployeeID), foreign key (CustomerID) references Customer(CustomerID));
create table Reservation (SDate Date, EDate Date, Price float(4,2), RoomNo int(3), CustomerID int(10), SerialNo int(10) primary key, Members int(2), EmployeeID int (10), isPaid boolean, foreign key (RoomNo) references Room(RoomNo), foreign key (EmployeeID) references Employee(EmployeeID), foreign key (CustomerID) references Customer(CustomerID));
