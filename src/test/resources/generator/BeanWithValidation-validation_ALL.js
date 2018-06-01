/**
 * Generated Time:Fri Jun 01 17:08:01 GMT+08:00 2018
 */
Ext.define("ch.rasc.extclassgenerator.bean.BeanWithValidation",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.validator.Email", "Ext.data.validator.Format", "Ext.data.validator.Presence", "Ext.data.validator.Range" ],
  fields : [ {
    name : "email",
    type : "string",
    validators : [ {
      type : "email"
    }, {
      type : "presence"
    } ]
  }, {
    name : "url",
    type : "string",
    validators : [ {
      type : "format",
      matcher : /^\/\w.*\.\w.*/
    } ]
  }, {
    name : "minMax1",
    type : "number",
    validators : [ {
      type : "range",
      max : 100
    } ]
  }, {
    name : "minMax2",
    type : "integer",
    validators : [ {
      type : "range",
      max : 10000
    } ]
  }, {
    name : "minMax3",
    type : "integer",
    validators : [ {
      type : "range",
      min : 20,
      max : 50
    } ]
  }, {
    name : "digits",
    type : "string",
    validators : [ {
      type : "digits",
      integer : 10,
      fraction : 2
    } ]
  }, {
    name : "future",
    type : "date",
    validators : [ {
      type : "future"
    } ]
  }, {
    name : "past",
    type : "date"
  }, {
    name : "notBlank",
    type : "string",
    validators : [ {
      type : "notBlank"
    } ]
  }, {
    name : "creditCardNumber",
    type : "string",
    validators : [ {
      type : "creditCardNumber"
    } ]
  } ]
});