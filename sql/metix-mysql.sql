# -------------------------------------------------------------------------------
# Primary key ranges:
#
# 10000-10099  IritgoUser
# 10100-10199  MetixUser
# 10200-10299  User profiles
# 10300-10399  User preferences
# 11000-19999  Singleton objects
# 12000-12999  Sample gaging stations
# 13000-13999  Sample gaging sensors
# 14000-14999  Sample gaging systems
# 15000-15999  Sample gaging system sensors
# 16000-16999  Physical interfaces
# -------------------------------------------------------------------------------


# -------------------------------------------------------------------------------
# Drop old database and user
# -------------------------------------------------------------------------------

delete from mysql.db where user='metix' and host='localhost';

delete from mysql.user where user='metix' and host='localhost';

drop database if exists metix;


# -------------------------------------------------------------------------------
# Create database and user
# -------------------------------------------------------------------------------

create database metix;

connect metix;

grant all privileges on metix.* to 'metix'@'localhost' identified by 'metix';

flush privileges;


# -------------------------------------------------------------------------------
# Create Tables
# -------------------------------------------------------------------------------

create table IritgoProperties
(
	name varchar (64) not null primary key,
	value varchar (80)
);

create table IritgoObjectList
(
	type varchar (64),
	id bigint,
	attribute varchar (64),
	elemType varchar (64),
	elemId bigint
);

create table IritgoUser
(
	id bigint not null primary key,
	name varchar (80),
	password varchar (80),
	email varchar (80)
);

create table IritgoNamedObjects
(
	userId bigint,
	name varchar (80),
	id bigint
);

create table MetixUserRegistry
(
	id bigint not null primary key
);

create table MetixUser
(
	id bigint not null primary key,
	name varchar (80),
	password varchar (80),
	email varchar (80),
	role int
);

create table UserProfile
(
	id bigint not null primary key,
	desktopList text,
	activeDesktopId varchar (80)
);

create table Preferences
(
	id bigint not null primary key,
	language varchar (16),
	lookAndFeel varchar (32),
	alwaysDrawWindowContents integer,
	drawAntiAliased integer,
	alignWindowsToRaster integer,
	rasterSize integer
);

create table ActiveDisplay
(
	id bigint not null primary key,
	displayGUIPaneId varchar (80),
	displayTypeId varchar (80),
	displayUniqueId bigint,
	displayX integer,
	displayY integer,
	displayWidth integer,
	displayHeight integer,
	displayMaximized integer,
	desktopId varchar (80),
	deleted integer
);

create table Measurement
(
	id bigint,
	at datetime,
	value double,
	stationId bigint,
	sensorId bigint,
 	key at (at),
 	key stationId (stationId),
 	key sensorId (sensorId)
);

create table GagingStationRegistry
(
	id bigint not null primary key
);

create table GagingStation
(
	id bigint not null primary key,
	name varchar (80),
	location varchar (80),
	longitude varchar (80),
	latitude varchar (80)
);

create table GagingSensor
(
	id bigint not null primary key,
	name varchar (80),
	dimension varchar (32),
	unit varchar (32),
	behaviourId varchar (32),
	systemId bigint,
	outputId bigint,
	period double,
	periodType integer,
	inputs varchar (255)
);

create table InterfaceRegistry
(
	id bigint not null primary key
);

create table GagingSystem
(
	id bigint not null primary key,
	name varchar (80),
	active integer,
	storeToDatabase	integer,
	interfaceId bigint,
	driverId varchar (128),
	driverParams text
);

create table GagingOutput
(
	id bigint not null primary key,
	name varchar (80),
	dimension varchar (32),
	unit varchar (32),
	customParams text
);

create table Interface
(
	id bigint not null primary key,
	name varchar (80),
	driverId varchar (128),
	driverParams text
);

create table SimpleInstrument
(
	id bigint not null primary key,
	fresh integer,
	title varchar (80),
	fitToWindow integer,
	font varchar (64),
	textColor integer,
	backgroundColor integer,
	transparent integer
);

create table SimpleInstrumentSensor
(
	id bigint not null primary key,
	sensorId bigint,
	stationId bigint,
	sensorListenerId varchar (32),
	startDate bigint,
	stopDate bigint,
	warnMin integer,
	warnMax integer,
	warnMinValue double,
	warnMaxValue double,
	color integer,
	font varchar (64)
);

create table BarInstrument
(
	id bigint not null primary key,
	fresh integer,
	title varchar (80),
	fitToWindow integer,
	scalinglow double,
	scalingHigh double,
	axisLogarithmic integer,
	showMaxMarker integer,
	showMinMarker integer, 
	showAxisLabel integer,
	showScaleLabel integer,
	dimensionColor integer,
	dimensionFont varchar (64),
	maxMarkerColor integer,
	minMarkerColor integer,
	barColor integer,
	font varchar (64)
);

create table BarInstrumentSensor
(
	id bigint not null primary key,
	sensorId bigint,
	stationId bigint,
	sensorListenerId varchar (32),
	startDate bigint,
	stopDate bigint,
	warnMin integer,
	warnMax integer,
	warnMinValue double,
	warnMaxValue double,
	color integer,
	font varchar (64)
);

create table LineChart
(
	id bigint not null primary key,
	fresh integer,
	fitToWindow integer,
	title varchar (80),
	showDomainRasterLines integer,
	showDomainLabels integer,
	showDomainTickLabels integer,
	showRangeRasterLines integer,
	showRangeLabels integer,
	showRangeTickLabels integer,
	showLegend integer,
	domainAxisMode integer,
	domainStartDate bigint,
	domainStopDate bigint,
	currentDomainCount double,
	currentDomainUnits integer,
	gridColor integer,
	backgroundColor integer,
	font varchar (64)
);

create table LineChartSensor
(
	id bigint not null primary key,
	sensorId bigint,
	stationId bigint,
	title varchar (80),
	sensorListenerId varchar (32),
	startDate bigint,
	stopDate bigint,
	sensorNr integer,
	axisRangeMode integer,
	axisRangeStart double,
	axisRangeStop double,
	axisTakeFrom integer,
	warnMin integer,
	warnMax integer,
	warnMinValue double,
	warnMaxValue double,
	color integer,
	font varchar (64)
);

create table ListInstrument
(
	id bigint not null primary key,
	fresh integer,
	title varchar (80),
	fitToWindow integer,
	showStationColumn integer,
	showDateColumn integer,
	backgroundColor integer,
	headerFont varchar (64)
);

create table ListInstrumentSensor
(
	id bigint not null primary key,
	sensorId bigint,
	stationId bigint,
	stationName varchar (80),
	sensorName varchar (80),
	sensorListenerId varchar (32),
	startDate bigint,
	stopDate bigint,
	warnMin integer,
	warnMax integer,
	warnMinValue double,
	warnMaxValue double,
	color integer,
	font varchar (64)
);

create table RoundInstrument
(
	id bigint not null primary key,
	fresh integer,
	title varchar (80),
	fitToWindow integer,
	showStationColumn integer,
	showDateColumn integer,
	scalingLow double,
	scalingHigh double,
	division double,
	divisionType integer,
	showMinMarker integer,
	showMaxMarker integer,
	showInscription integer,
	showDigital integer,
	displayMode integer,
	dimensionColor integer,
	dimensionFont varchar (64),
	maxMarkerColor integer,
	minMarkerColor integer,
	font varchar (64),
	needleColor integer
);

create table RoundInstrumentSensor
(
	id bigint not null primary key,
	sensorId bigint,
	stationId bigint,
	sensorListenerId varchar (32),
	startDate bigint,
	stopDate bigint,
	warnMin integer,
	warnMax integer,
	warnMinValue double,
	warnMaxValue double,
	color integer,
	font varchar (64)
);

create table WebInstrument
(
	id bigint not null primary key,
	fresh integer,
	title varchar (80),
	fitToWindow integer,
	url varchar (255),
	reloadInterval integer,
	backgroundColor integer
);

create table WebInstrumentSensor
(
	id bigint not null primary key,
	sensorId bigint,
	stationId bigint,
	sensorListenerId varchar (32),
	startDate bigint,
	stopDate bigint,
	warnMin integer,
	warnMax integer,
	warnMinValue double,
	warnMaxValue double,
	color integer,
	font varchar (64)
);

create table WindRoseInstrument
(
	id bigint not null primary key,
	fresh integer,
	title varchar (80),
	fitToWindow integer,
	textColor integer,
	font varchar (64),
	needleType integer,
	needleColor integer,
	roseColor integer,
	minExtrema integer,
	hourExtrema integer,
	minExtremaColor integer,
	hourExtremaColor integer,
	reverseNeedle integer
);

create table WindRoseInstrumentSensor
(
	id bigint not null primary key,
	sensorId bigint,
	stationId bigint,
	sensorListenerId varchar (32),
	startDate bigint,
	stopDate bigint,
	warnMin integer,
	warnMax integer,
	warnMinValue double,
	warnMaxValue double,
	color integer,
	font varchar (64)
);

create table Licensing
(
	kc_id bigint not null primary key,
	kc_name varchar (80),
	kc_key varchar(80),
	kc_entry integer
);



# -------------------------------------------------------------------------------
# System and sample data
# -------------------------------------------------------------------------------

insert into Licensing (kc_id, kc_name,kc_key,kc_entry) values
	('1','Metix Software','wt6792Me360174Si','0');

insert into IritgoProperties (name, value) values
	('persist.ids.nextvalue', '1000000');

insert into IritgoUser (id, name, password, email) values
	(10000, 'admin', 'admin', 'admin@metix.de');
insert into IritgoUser (id, name, password, email) values
	(10002, 'metix1', 'metix', 'metix1@metix.de');
insert into IritgoUser (id, name, password, email) values
	(10004, 'metix2', 'metix', 'metix2@metix.de');
insert into IritgoUser (id, name, password, email) values
	(10006, 'demo', 'demo', 'demo@metix.de');

insert into MetixUserRegistry (id) values 
	(11000);

insert into MetixUser (id, name, password, email, role) values
	(10100, 'admin', 'admin', 'admin@metix.de', 2);
insert into MetixUser (id, name, password, email, role) values
	(10102, 'metix1', 'metix1', 'metix1@metix.de', 0);
insert into MetixUser (id, name, password, email, role) values
	(10104, 'metix2', 'metix2', 'metix2@metix.de', 0);
insert into MetixUser (id, name, password, email, role) values
	(10106, 'demo', 'demo', 'demo@metix.de', 0);

insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('MetixUserRegistry', 11000, 'users', 'MetixUser', 10100); 
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('MetixUserRegistry', 11000, 'users', 'MetixUser', 10102); 
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('MetixUserRegistry', 11000, 'users', 'MetixUser', 10104);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('MetixUserRegistry', 11000, 'users', 'MetixUser', 10106);

insert into UserProfile (id, desktopList, activeDesktopId) values 
	(10200, 'empty', 'unknown');
insert into UserProfile (id, desktopList, activeDesktopId) values 
	(10202, 'empty', 'unknown');
insert into UserProfile (id, desktopList, activeDesktopId) values 
	(10204, 'empty', 'unknown');
insert into UserProfile (id, desktopList, activeDesktopId) values 
	(10206, 'empty', 'unknown');

insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('MetixUserRegistry', 11000, 'profiles', 'UserProfile', 10200); 
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('MetixUserRegistry', 11000, 'profiles', 'UserProfile', 10202); 
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('MetixUserRegistry', 11000, 'profiles', 'UserProfile', 10204);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('MetixUserRegistry', 11000, 'profiles', 'UserProfile', 10206);

insert into Preferences (id, language, lookAndFeel, alwaysDrawWindowContents, drawAntiAliased, alignWindowsToRaster, rasterSize) values
	(10300, 'de', 'com.jgoodies.plaf.plastic.theme.KDE', 1, 0, 1, 20);
insert into Preferences (id, language, lookAndFeel, alwaysDrawWindowContents, drawAntiAliased, alignWindowsToRaster, rasterSize) values
	(10302, 'de', 'com.jgoodies.plaf.plastic.theme.KDE', 1, 0, 1, 20);
insert into Preferences (id, language, lookAndFeel, alwaysDrawWindowContents, drawAntiAliased, alignWindowsToRaster, rasterSize) values
	(10304, 'de', 'com.jgoodies.plaf.plastic.theme.KDE', 1, 0, 1, 20);
insert into Preferences (id, language, lookAndFeel, alwaysDrawWindowContents, drawAntiAliased, alignWindowsToRaster, rasterSize) values
	(10306, 'de', 'com.jgoodies.plaf.plastic.theme.KDE', 1, 0, 1, 20);

insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('UserProfile', 10200, 'preferences', 'Preferences', 10300); 
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('UserProfile', 10202, 'preferences', 'Preferences', 10302); 
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('UserProfile', 10204, 'preferences', 'Preferences', 10304);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('UserProfile', 10206, 'preferences', 'Preferences', 10306);

insert into GagingStationRegistry (id) values 
	(11002);

insert into GagingStation (id, name, location, longitude, latitude) values
	(12000, 'Dortmund', 'Dortmund', '51° Nord', '7° Ost');
insert into GagingStation (id, name, location, longitude, latitude) values
	(12002, 'Moskau', 'Moskau', '55° Nord', '38° Ost');
insert into GagingStation (id, name, location, longitude, latitude) values
	(12004, 'New York', 'New York', '40° Nord', '75° West');
insert into GagingStation (id, name, location, longitude, latitude) values
	(12006, 'Sidney', 'Sidney', '35° Süd', '150° Ost');
insert into GagingStation (id, name, location, longitude, latitude) values
	(12008, 'Panama', 'Panama', '10° Nord', '80° West');
insert into GagingStation (id, name, location, longitude, latitude) values
	(12010, 'Lokal', 'Lokal', 'Lokal', 'Lokal');

insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStationRegistry', 11002, 'gagingStations', 'GagingStation', 12000);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStationRegistry', 11002, 'gagingStations', 'GagingStation', 12002);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStationRegistry', 11002, 'gagingStations', 'GagingStation', 12004);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStationRegistry', 11002, 'gagingStations', 'GagingStation', 12006);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStationRegistry', 11002, 'gagingStations', 'GagingStation', 12008);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStationRegistry', 11002, 'gagingStations', 'GagingStation', 12010);

insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13000, 'Temperatur', 'temperature', 'c', 'SystemSensorBehaviour', 14002, 15100, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13002, 'Temperatur Min', 'temperature', 'c', 'MinimumSensorBehaviour', 0, 0, 1, 5, '13000');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13004, 'Temperatur Max', 'temperature', 'c', 'MaximumSensorBehaviour', 0, 0, 1, 5, '13000');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13006, 'Temperatur Mittel', 'temperature', 'c', 'AverageSensorBehaviour', 0, 0, 1, 5, '13000');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13008, 'Windstärke', 'windgust', 'ms', 'SystemSensorBehaviour', 14002, 15102, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13010, 'Windrichtung', 'winddirection', 'degrees', 'SystemSensorBehaviour', 14002, 15104, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13012, 'Feuchte', 'relhumidity', 'percent', 'SystemSensorBehaviour', 14002, 15106, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13014, 'Druck', 'barometricpressure', 'hpa', 'SystemSensorBehaviour', 14002, 15108, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13016, 'Niederschlag', 'heightofprecipitation', 'mm', 'SystemSensorBehaviour', 14002, 15110, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13018, 'Taupunkt', 'temperature', 'c', 'DewpointSensorBehaviour', 0, 0, 1, 5, '13000,13012');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13100, 'Temperatur', 'temperature', 'c', 'SystemSensorBehaviour', 14008, 15400, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13102, 'Windstärke', 'windgust', 'ms', 'SystemSensorBehaviour', 14008, 15402, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13104, 'Windrichtung', 'winddirection', 'degrees', 'SystemSensorBehaviour', 14008, 15404, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13106, 'Feuchte', 'relhumidity', 'percent', 'SystemSensorBehaviour', 14008, 15406, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13108, 'Druck', 'barometricpressure', 'hpa', 'SystemSensorBehaviour', 14008, 15408, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13110, 'Niederschlag', 'heightofprecipitation', 'mm', 'SystemSensorBehaviour', 14008, 15410, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13200, 'Temperatur', 'temperature', 'c', 'SystemSensorBehaviour', 14004, 15200, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13202, 'Windstärke', 'windgust', 'ms', 'SystemSensorBehaviour', 14004, 15202, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13204, 'Windrichtung', 'winddirection', 'degrees', 'SystemSensorBehaviour', 14004, 15204, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13206, 'Feuchte', 'relhumidity', 'percent', 'SystemSensorBehaviour', 14004, 15206, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13208, 'Druck', 'barometricpressure', 'hpa', 'SystemSensorBehaviour', 14004, 15208, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13210, 'Niederschlag', 'heightofprecipitation', 'mm', 'SystemSensorBehaviour', 14004, 15210, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13300, 'Temperatur', 'temperature', 'c', 'SystemSensorBehaviour', 14006, 15300, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13302, 'Windstärke', 'windgust', 'ms', 'SystemSensorBehaviour', 14006, 15302, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13304, 'Windrichtung', 'winddirection', 'degrees', 'SystemSensorBehaviour', 14006, 15304, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13306, 'Feuchte', 'relhumidity', 'percent', 'SystemSensorBehaviour', 14006, 15306, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13308, 'Druck', 'barometricpressure', 'hpa', 'SystemSensorBehaviour', 14006, 15308, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13310, 'Niederschlag', 'heightofprecipitation', 'mm', 'SystemSensorBehaviour', 14006, 15310, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13400, 'Temperatur', 'temperature', 'c', 'SystemSensorBehaviour', 14010, 15500, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13402, 'Windstärke', 'windgust', 'ms', 'SystemSensorBehaviour', 14010, 15502, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13404, 'Windrichtung', 'winddirection', 'degrees', 'SystemSensorBehaviour', 14010, 15504, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13406, 'Feuchte', 'relhumidity', 'percent', 'SystemSensorBehaviour', 14010, 15506, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13408, 'Druck', 'barometricpressure', 'hpa', 'SystemSensorBehaviour', 14010, 15508, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13410, 'Niederschlag', 'heightofprecipitation', 'mm', 'SystemSensorBehaviour', 14010, 15510, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13500, 'Temperatur', 'temperature', 'c', 'SystemSensorBehaviour', 14012, 15600, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13502, 'Feuchte', 'relhumidity', 'percent', 'SystemSensorBehaviour', 14012, 15602, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13504, 'Windstärke', 'windgust', 'ms', 'SystemSensorBehaviour', 14012, 15604, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13506, 'Windrichtung', 'winddirection', 'degrees', 'SystemSensorBehaviour', 14012, 15606, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13508, 'Strahlung', 'temperature', 'c', 'SystemSensorBehaviour', 14012, 15608, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13510, 'Druck', 'barometricpressure', 'hpa', 'SystemSensorBehaviour', 14012, 15610, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13512, 'Widerstand 1', 'relhumidity', 'percent', 'SystemSensorBehaviour', 14012, 15612, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13514, 'Widerstand 2', 'relhumidity', 'percent', 'SystemSensorBehaviour', 14012, 15614, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13516, 'Niederschlag', 'heightofprecipitation', 'mm', 'SystemSensorBehaviour', 14012, 15616, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13518, 'Zustand', 'relhumidity', 'percent', 'SystemSensorBehaviour', 14012, 15618, 0, 0, '');
insert into GagingSensor (id, name, dimension, unit, behaviourId, systemId, outputId, period, periodType, inputs) values
	(13520, 'Taupunkt', 'dewpoint', 'c', 'SystemSensorBehaviour', 14012, 15620, 0, 0, '');

insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12000, 'sensors', 'GagingSensor', 13000);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12000, 'sensors', 'GagingSensor', 13002);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12000, 'sensors', 'GagingSensor', 13004);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12000, 'sensors', 'GagingSensor', 13006);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12000, 'sensors', 'GagingSensor', 13008);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12000, 'sensors', 'GagingSensor', 13010);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12000, 'sensors', 'GagingSensor', 13012);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12000, 'sensors', 'GagingSensor', 13014);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12000, 'sensors', 'GagingSensor', 13016);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12000, 'sensors', 'GagingSensor', 13018);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12002, 'sensors', 'GagingSensor', 13100);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12002, 'sensors', 'GagingSensor', 13102);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12002, 'sensors', 'GagingSensor', 13104);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12002, 'sensors', 'GagingSensor', 13106);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12002, 'sensors', 'GagingSensor', 13108);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12002, 'sensors', 'GagingSensor', 13110);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12004, 'sensors', 'GagingSensor', 13200);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12004, 'sensors', 'GagingSensor', 13202);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12004, 'sensors', 'GagingSensor', 13204);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12004, 'sensors', 'GagingSensor', 13206);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12004, 'sensors', 'GagingSensor', 13208);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12004, 'sensors', 'GagingSensor', 13210);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12006, 'sensors', 'GagingSensor', 13300);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12006, 'sensors', 'GagingSensor', 13302);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12006, 'sensors', 'GagingSensor', 13304);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12006, 'sensors', 'GagingSensor', 13306);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12006, 'sensors', 'GagingSensor', 13308);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12006, 'sensors', 'GagingSensor', 13310);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12008, 'sensors', 'GagingSensor', 13400);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12008, 'sensors', 'GagingSensor', 13402);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12008, 'sensors', 'GagingSensor', 13404);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12008, 'sensors', 'GagingSensor', 13406);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12008, 'sensors', 'GagingSensor', 13408);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12008, 'sensors', 'GagingSensor', 13410);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12010, 'sensors', 'GagingSensor', 13500);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12010, 'sensors', 'GagingSensor', 13502);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12010, 'sensors', 'GagingSensor', 13504);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12010, 'sensors', 'GagingSensor', 13506);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12010, 'sensors', 'GagingSensor', 13508);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12010, 'sensors', 'GagingSensor', 13510);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12010, 'sensors', 'GagingSensor', 13512);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12010, 'sensors', 'GagingSensor', 13514);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12010, 'sensors', 'GagingSensor', 13516);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12010, 'sensors', 'GagingSensor', 13518);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingStation', 12010, 'sensors', 'GagingSensor', 13520);

insert into InterfaceRegistry (id) values 
	(11004);

insert into Interface (id, name, driverId, driverParams) values
	(16000, 'Null', 'NullDriver', '');
insert into Interface (id, name, driverId, driverParams) values
	(16002, 'Seriell 1 (Windows)', 'SerialDriver', 'port=COM1\nbaudRate=19200\ndataBits=8\nparity=N\nstopBits=1');
insert into Interface (id, name, driverId, driverParams) values
	(16004, 'Seriell 2 (Windows)', 'SerialDriver', 'port=COM2\nbaudRate=19200\ndataBits=8\nparity=N\nstopBits=1');
insert into Interface (id, name, driverId, driverParams) values
	(16006, 'Seriell 1 (Unix)', 'SerialDriver', 'port=/dev/ttyS0\nbaudRate=19200\ndataBits=8\nparity=N\nstopBits=1');
insert into Interface (id, name, driverId, driverParams) values
	(16008, 'Seriell 2 (Unix)', 'SerialDriver', 'port=/dev/ttyS1\nbaudRate=19200\ndataBits=8\nparity=N\nstopBits=1');

insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('InterfaceRegistry', 11004, 'interfaces', 'Interface', 16000);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('InterfaceRegistry', 11004, 'interfaces', 'Interface', 16002);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('InterfaceRegistry', 11004, 'interfaces', 'Interface', 16004);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('InterfaceRegistry', 11004, 'interfaces', 'Interface', 16006);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('InterfaceRegistry', 11004, 'interfaces', 'Interface', 16008);

insert into GagingSystem (id, name, active, storeToDatabase, interfaceId, driverId, driverParams) values
	(14000, '---', 0, 0, 16000, 'Null', '');
insert into GagingSystem (id, name, active, storeToDatabase, interfaceId, driverId, driverParams) values
	(14002, 'Weather Simulator 1', 0, 0, 16000, 'WeatherSimulator', 'interval=1\nout0min=-8.0\nout0max=30.0\nout0var=0.1\nout1min=0.0\nout1max=50.0\nout1var=2.0\nout2min=0.0\nout2max=359.99\nout2var=15.0\nout3min=50.0\nout3max=100.0\nout3var=1.5\nout4min=1000.0\nout4max=1030.0\nout4var=0.1\nout5min=50.0\nout5max=100.0\nout5var=0.05');
insert into GagingSystem (id, name, active, storeToDatabase, interfaceId, driverId, driverParams) values
	(14004, 'Weather Simulator 2', 0, 0, 16000, 'WeatherSimulator', 'interval=1\nout0min=-10.0\nout0max=32.0\nout0var=0.1\nout1min=0.0\nout1max=50.0\nout1var=2.0\nout2min=0.0\nout2max=359.99\nout2var=15.0\nout3min=30.0\nout3max=100.0\nout3var=1.5\nout4min=1000.0\nout4max=1030.0\nout4var=0.2\nout5min=100.0\nout5max=200.0\nout5var=0.05');
insert into GagingSystem (id, name, active, storeToDatabase, interfaceId, driverId, driverParams) values
	(14006, 'Weather Simulator 3', 0, 0, 16000, 'WeatherSimulator', 'interval=1\nout0min=0.0\nout0max=40.0\nout0var=0.1\nout1min=0.0\nout1max=50.0\nout1var=2.0\nout2min=0.0\nout2max=359.99\nout2var=15.0\nout3min=0.0\nout3max=400.0\nout3var=1.5\nout4min=1020.0\nout4max=1030.0\nout4var=0.3\nout5min=0.0\nout5max=50.0\nout5var=0.05');
insert into GagingSystem (id, name, active, storeToDatabase, interfaceId, driverId, driverParams) values
	(14008, 'Weather Simulator 4', 0, 0, 16000, 'WeatherSimulator', 'interval=1\nout0min=-8.0\nout0max=30.0\nout0var=0.1\nout1min=0.0\nout1max=50.0\nout1var=2.0\nout2min=0.0\nout2max=359.99\nout2var=15.0\nout3min=50.0\nout3max=100.0\nout3var=1.5\nout4min=1000.0\nout4max=1030.0\nout4var=0.1\nout5min=50.0\nout5max=100.0\nout5var=0.05');
insert into GagingSystem (id, name, active, storeToDatabase, interfaceId, driverId, driverParams) values
	(14010, 'Weather Simulator 5', 0, 0, 16000, 'WeatherSimulator', 'interval=1\nout0min=-10.0\nout0max=32.0\nout0var=0.1\nout1min=0.0\nout1max=50.0\nout1var=2.0\nout2min=0.0\nout2max=359.99\nout2var=15.0\nout3min=30.0\nout3max=100.0\nout3var=1.5\nout4min=1000.0\nout4max=1030.0\nout4var=0.2\nout5min=100.0\nout5max=200.0\nout5var=0.05');
insert into GagingSystem (id, name, active, storeToDatabase, interfaceId, driverId, driverParams) values
	(14012, 'Lambrecht Synmet', 0, 0, 16004, 'LambrechtSynmet', 'interval=1');
insert into GagingSystem (id, name, active, storeToDatabase, interfaceId, driverId, driverParams) values
	(14014, 'Friedrichs Combilog', 0, 0, 16002, 'FriedrichsCombilog', 'interval=1');
insert into GagingSystem (id, name, active, storeToDatabase, interfaceId, driverId, driverParams) values
	(14016, 'Reinhardt MWS9', 0, 0, 16002, 'ReinhardtMSW9', 'interval=1');

insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('InterfaceRegistry', 11004, 'gagingSystems', 'GagingSystem', 14000);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('InterfaceRegistry', 11004, 'gagingSystems', 'GagingSystem', 14002);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('InterfaceRegistry', 11004, 'gagingSystems', 'GagingSystem', 14004);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('InterfaceRegistry', 11004, 'gagingSystems', 'GagingSystem', 14006);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('InterfaceRegistry', 11004, 'gagingSystems', 'GagingSystem', 14008);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('InterfaceRegistry', 11004, 'gagingSystems', 'GagingSystem', 14010);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('InterfaceRegistry', 11004, 'gagingSystems', 'GagingSystem', 14012);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('InterfaceRegistry', 11004, 'gagingSystems', 'GagingSystem', 14014);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('InterfaceRegistry', 11004, 'gagingSystems', 'GagingSystem', 14016);

insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15000, '---', '', '', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15100, 'Temperatur', 'temperature', 'c', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15102, 'Windstärke', 'windgust', 'ms', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15104, 'Windrichtung', 'winddirection', 'degrees', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15106, 'Feuchte', 'relhumidity', 'percent', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15108, 'Druck', 'barometricpressure', 'hpa', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15110, 'Niederschlag', 'heightofprecipitation', 'lm2', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15200, 'Temperatur', 'temperature', 'c', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15202, 'Windstärke', 'windgust', 'ms', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15204, 'Windrichtung', 'winddirection', 'degrees', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15206, 'Feuchte', 'relhumidity', 'percent', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15208, 'Druck', 'barometricpressure', 'hpa', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15210, 'Niederschlag', 'heightofprecipitation', 'lm2', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15300, 'Temperatur', 'temperature', 'c', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15302, 'Windstärke', 'windgust', 'ms', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15304, 'Windrichtung', 'winddirection', 'degrees', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15306, 'Feuchte', 'relhumidity', 'percent', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15308, 'Druck', 'barometricpressure', 'hpa', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15310, 'Niederschlag', 'heightofprecipitation', 'lm2', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15400, 'Temperatur', 'temperature', 'c', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15402, 'Windstärke', 'windgust', 'ms', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15404, 'Windrichtung', 'winddirection', 'degrees', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15406, 'Feuchte', 'relhumidity', 'percent', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15408, 'Druck', 'barometricpressure', 'hpa', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15410, 'Niederschlag', 'heightofprecipitation', 'lm2', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15500, 'Temperatur', 'temperature', 'c', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15502, 'Windstärke', 'windgust', 'ms', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15504, 'Windrichtung', 'winddirection', 'degrees', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15506, 'Feuchte', 'relhumidity', 'percent', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15508, 'Druck', 'barometricpressure', 'hpa', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15510, 'Niederschlag', 'heightofprecipitation', 'lm2', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15600, 'Temperatur', 'temperature', 'c', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15602, 'Feuchte', 'relhumidity', 'percent', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15604, 'Windstärke', 'windgust', 'ms', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15606, 'Windrichtung', 'winddirection', 'degrees', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15608, 'Strahlung', 'temperature', 'c', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15610, 'Druck', 'barometricpressure', 'hpa', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15612, 'Widerstand 1', 'relhumidity', 'percent', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15614, 'Widerstand 2', 'relhumidity', 'percent', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15616, 'Niederschlag', 'heightofprecipitation', 'mm', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15618, 'Zustand', 'relhumidity', 'percent', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15620, 'Taupunkt', 'dewpoint', 'c', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15700, 'Temperatur', 'temperature', 'c', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15702, 'Windstärke', 'windgust', 'ms', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15704, 'Windrichtung', 'winddirection', 'degrees', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15706, 'Feuchte', 'relhumidity', 'percent', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15708, 'Druck', 'barometricpressure', 'hpa', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15710, 'Niederschlag', 'heightofprecipitation', 'lm2', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15712, 'Temperatur', 'temperature', 'c', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15714, 'Luftdruck', 'barometricpressure', 'mbar', '');
insert into GagingOutput (id, name, dimension, unit, customParams) values
	(15716, 'Feuchte', 'relhumidity', 'percent', '');

insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14000, 'outputs', 'GagingOutput', 15000);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14002, 'outputs', 'GagingOutput', 15100);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14002, 'outputs', 'GagingOutput', 15102);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14002, 'outputs', 'GagingOutput', 15104);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14002, 'outputs', 'GagingOutput', 15106);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14002, 'outputs', 'GagingOutput', 15108);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14002, 'outputs', 'GagingOutput', 15110);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14004, 'outputs', 'GagingOutput', 15200);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14004, 'outputs', 'GagingOutput', 15202);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14004, 'outputs', 'GagingOutput', 15204);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14004, 'outputs', 'GagingOutput', 15206);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14004, 'outputs', 'GagingOutput', 15208);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14004, 'outputs', 'GagingOutput', 15210);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14006, 'outputs', 'GagingOutput', 15300);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14006, 'outputs', 'GagingOutput', 15302);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14006, 'outputs', 'GagingOutput', 15304);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14006, 'outputs', 'GagingOutput', 15306);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14006, 'outputs', 'GagingOutput', 15308);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14006, 'outputs', 'GagingOutput', 15310);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14008, 'outputs', 'GagingOutput', 15400);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14008, 'outputs', 'GagingOutput', 15402);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14008, 'outputs', 'GagingOutput', 15404);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14008, 'outputs', 'GagingOutput', 15406);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14008, 'outputs', 'GagingOutput', 15408);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14008, 'outputs', 'GagingOutput', 15410);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14010, 'outputs', 'GagingOutput', 15500);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14010, 'outputs', 'GagingOutput', 15502);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14010, 'outputs', 'GagingOutput', 15504);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14010, 'outputs', 'GagingOutput', 15506);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14010, 'outputs', 'GagingOutput', 15508);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14010, 'outputs', 'GagingOutput', 15510);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14012, 'outputs', 'GagingOutput', 15600);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14012, 'outputs', 'GagingOutput', 15602);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14012, 'outputs', 'GagingOutput', 15604);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14012, 'outputs', 'GagingOutput', 15606);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14012, 'outputs', 'GagingOutput', 15608);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14012, 'outputs', 'GagingOutput', 15610);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14012, 'outputs', 'GagingOutput', 15612);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14012, 'outputs', 'GagingOutput', 15614);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14012, 'outputs', 'GagingOutput', 15616);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14012, 'outputs', 'GagingOutput', 15618);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14012, 'outputs', 'GagingOutput', 15620);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14014, 'outputs', 'GagingOutput', 15700);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14014, 'outputs', 'GagingOutput', 15702);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14014, 'outputs', 'GagingOutput', 15704);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14014, 'outputs', 'GagingOutput', 15706);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14014, 'outputs', 'GagingOutput', 15708);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14014, 'outputs', 'GagingOutput', 15710);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14016, 'outputs', 'GagingOutput', 15712);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14016, 'outputs', 'GagingOutput', 15714);
insert into IritgoObjectList (type, id, attribute, elemType, elemId) values
	('GagingSystem', 14016, 'outputs', 'GagingOutput', 15716);


insert into IritgoNamedObjects (userId, name, id) values 
	(10000, 'MetixUser', 10100);
insert into IritgoNamedObjects (userId, name, id) values 
	(10000, 'UserProfile', 10200);
insert into IritgoNamedObjects (userId, name, id) values 
	(10000, 'Preferences', 10300);
insert into IritgoNamedObjects (userId, name, id) values 
	(10000, 'MetixUserRegistry', 11000);
insert into IritgoNamedObjects (userId, name, id) values 
	(10000, 'GagingStationRegistry', 11002);
insert into IritgoNamedObjects (userId, name, id) values 
	(10000, 'InterfaceRegistry', 11004);
insert into IritgoNamedObjects (userId, name, id) values 
	(10002, 'MetixUser', 10102);
insert into IritgoNamedObjects (userId, name, id) values 
	(10002, 'UserProfile', 10202);
insert into IritgoNamedObjects (userId, name, id) values 
	(10002, 'Preferences', 10302);
insert into IritgoNamedObjects (userId, name, id) values 
	(10002, 'MetixUserRegistry', 11000);
insert into IritgoNamedObjects (userId, name, id) values 
	(10002, 'GagingStationRegistry', 11002);
insert into IritgoNamedObjects (userId, name, id) values 
	(10002, 'InterfaceRegistry', 11004);
insert into IritgoNamedObjects (userId, name, id) values 
	(10004, 'MetixUser', 10104);
insert into IritgoNamedObjects (userId, name, id) values 
	(10004, 'UserProfile', 10204);
insert into IritgoNamedObjects (userId, name, id) values 
	(10004, 'Preferences', 10304);
insert into IritgoNamedObjects (userId, name, id) values 
	(10004, 'MetixUserRegistry', 11000);
insert into IritgoNamedObjects (userId, name, id) values 
	(10004, 'GagingStationRegistry', 11002);
insert into IritgoNamedObjects (userId, name, id) values 
	(10004, 'InterfaceRegistry', 11004);
insert into IritgoNamedObjects (userId, name, id) values 
	(10006, 'MetixUser', 10106);
insert into IritgoNamedObjects (userId, name, id) values 
	(10006, 'UserProfile', 10206);
insert into IritgoNamedObjects (userId, name, id) values 
	(10006, 'Preferences', 10306);
insert into IritgoNamedObjects (userId, name, id) values 
	(10006, 'MetixUserRegistry', 11000);
insert into IritgoNamedObjects (userId, name, id) values 
	(10006, 'GagingStationRegistry', 11002);
insert into IritgoNamedObjects (userId, name, id) values 
	(10006, 'InterfaceRegistry', 11004);


# -------------------------------------------------------------------------------
# End
# -------------------------------------------------------------------------------

commit;
