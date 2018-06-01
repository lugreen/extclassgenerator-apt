/**
 * Generated Time:Fri Jun 01 17:08:20 GMT+08:00 2018
 */
Ext.define("ch.rasc.extclassgenerator.bean.UserClass",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.validator.Email", "Ext.data.validator.Length", "Ext.data.validator.Presence" ],
  fields : [ {
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