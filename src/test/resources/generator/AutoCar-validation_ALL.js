/**
 * Generated Time:Fri Jun 01 17:06:41 GMT+08:00 2018
 */
Ext.define("MyApp.model.Car",
{
  extend : "MyApp.model.Base",
  requires : [ "Ext.data.identifier.Negative" ],
  identifier : "negative",
  fields : [ "id", "name", {
    name : "manufacturerId",
    reference : "Manufacturer",
    allowBlank : false
  }, {
    name : "driverId",
    reference : {
      type : "Driver",
      association : "CarByDriver",
      role : "theDriver",
      inverse : "cars"
    }
  }, {
    name : "ownerId",
    reference : {
      parent : "Owner"
    }
  }, {
    name : "oneToOneId",
    reference : "OneToOne",
    unique : true
  } ]
});