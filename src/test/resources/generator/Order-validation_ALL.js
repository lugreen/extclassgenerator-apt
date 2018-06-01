/**
 * Generated Time:Fri Jun 01 17:06:41 GMT+08:00 2018
 */
Ext.define("ch.rasc.extclassgenerator.bean.Order",
{
  extend : "Ext.data.Model",
  uses : [ "ch.rasc.extclassgenerator.bean.Pos" ],
  fields : [ {
    name : "entityId",
    type : "integer"
  } ],
  associations : [ {
    type : "hasMany",
    model : "ch.rasc.extclassgenerator.bean.Pos",
    associationKey : "positions",
    foreignKey : "orderId",
    primaryKey : "entityId",
    name : "pos"
  } ]
});