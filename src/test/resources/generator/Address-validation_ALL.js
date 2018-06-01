/**
 * Generated Time:Fri Jun 01 17:06:40 GMT+08:00 2018
 */
Ext.define("ch.rasc.extclassgenerator.bean.Address",
{
  extend : "Ext.data.Model",
  uses : [ "MyApp.Employee" ],
  fields : [ {
    name : "id",
    type : "integer"
  }, {
    name : "e_id",
    type : "integer"
  } ],
  associations : [ {
    type : "hasOne",
    model : "MyApp.Employee",
    associationKey : "employee",
    foreignKey : "e_id",
    primaryKey : "eId",
    setterName : "setE",
    getterName : "getE",
    name : "emp"
  } ]
});