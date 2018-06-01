/**
 * Generated Time:Fri Jun 01 17:07:48 GMT+08:00 2018
 */
Ext.define("ch.rasc.extclassgenerator.bean.BeanWithGenericValidation",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.validator.Exclusion", "Ext.data.validator.Inclusion", "Ext.data.validator.Presence" ],
  fields : [ {
    name : "singleton",
    type : "string",
    validators : [ {
      type : "notUnique",
      update : "true"
    }, {
      type : "presence"
    } ]
  }, {
    name : "excluded",
    type : "string",
    validators : [ {
      type : "exclusion",
      list : [ "_" ]
    } ]
  }, {
    name : "excluded2",
    type : "string",
    validators : [ {
      type : "exclusion",
      list : [ "_", "*" ]
    } ]
  }, {
    name : "included",
    type : "string",
    validators : [ {
      type : "inclusion",
      list : [ "fish", "fruit" ]
    } ]
  }, {
    name : "excludedV1",
    type : "string",
    validators : [ {
      type : "exclusion",
      list : ["_"]
    } ]
  }, {
    name : "includedV1",
    type : "string",
    validators : [ {
      type : "inclusion",
      list : ["fish","fruit"]
    } ]
  } ]
});