Create table Bank_Account (
   ID Bigint not null,
   Full_Name Varchar(128) not null,
   Balance real not null,
   CONSTRAINT Bank_Account_pk PRIMARY KEY (ID)
);

Insert into Bank_Account(ID, Full_Name, Balance) values (1, 'Tom', 1000);
Insert into Bank_Account(ID, Full_Name, Balance) values (2, 'Jerry', 2000);
Insert into Bank_Account(ID, Full_Name, Balance) values (3, 'Donald', 3000);