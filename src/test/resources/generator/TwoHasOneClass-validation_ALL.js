/**
 * Generated Time:Fri Jun 01 17:06:41 GMT+08:00 2018
 */
Ext.define("App.TwoHasOneClass",
{
  extend : "Ext.data.Model",
  uses : [ "ch.rasc.extclassgenerator.bean.AssociatedClass" ],
  fields : [ ],
  associations : [ {
    type : "hasOne",
    model : "ch.rasc.extclassgenerator.bean.AssociatedClass",
    associationKey : "myFirstAssociation",
    foreignKey : "myFirstAssociation_id",
    setterName : "setMyFirstAssociation",
    getterName : "getMyFirstAssociation"
  }, {
    type : "hasOne",
    model : "ch.rasc.extclassgenerator.bean.AssociatedClass",
    associationKey : "mySecondAssociation",
    foreignKey : "mySecondAssociation_id",
    setterName : "setMySecondAssociation",
    getterName : "getMySecondAssociation"
  } ]
});