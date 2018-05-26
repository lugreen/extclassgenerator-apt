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
    type : "string"
  }, {
    name : "future",
    type : "date"
  }, {
    name : "past",
    type : "date"
  }, {
    name : "notBlank",
    type : "string"
  }, {
    name : "creditCardNumber",
    type : "string"
  } ]
});