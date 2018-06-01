/**
 * Generated Time:Fri Jun 01 17:06:43 GMT+08:00 2018
 */
Ext.define("MyApp.Employee",
{
  extend : "Ext.data.Model",
  uses : [ "ch.rasc.extclassgenerator.bean.Address" ],
  requires : [ "Ext.data.proxy.Direct", "Ext.data.identifier.Negative" ],
  identifier : "negative",
  idProperty : "eId",
  fields : [ {
    name : "eId",
    type : "integer"
  }, {
    name : "address_id",
    type : "integer"
  } ],
  associations : [ {
    type : "hasOne",
    model : "ch.rasc.extclassgenerator.bean.Address",
    associationKey : "address",
    foreignKey : "address_id",
    setterName : "setAddress",
    getterName : "getAddress"
  } ],
  proxy : {
    type : "direct",
    idParam : "eId",
    writer : {
      writeAllFields : true
    }
  }
});