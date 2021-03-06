/**
 * Generated Time:Fri Jun 01 17:08:14 GMT+08:00 2018
 */
Ext.define("ch.rasc.extclassgenerator.bean.User",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.validator.Email", "Ext.data.validator.Length", "Ext.data.validator.Presence" ],
  fields : [ "id", {
    name : "email",
    type : "string",
    validators : [ {
      type : "presence"
    }, {
      type : "email"
    }, {
      type : "length",
      min : 0,
      max : 128
    } ]
  } ]
});