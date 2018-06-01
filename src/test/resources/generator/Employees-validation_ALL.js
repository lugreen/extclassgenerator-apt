/**
 * Generated Time:Fri Jun 01 17:19:00 GMT+08:00 2018
 */
Ext.define("ch.rasc.extclassgenerator.bean.Employees",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.validator.Length" ],
  fields : [ {
    name : "employeeId",
    type : "integer",
    validators : [ {
      type : "length",
      max : 11
    } ],
    allowBlank : false,
    unique : true
  }, {
    name : "lastName",
    type : "string",
    validators : [ {
      type : "length",
      max : 20
    } ],
    allowBlank : false
  }, {
    name : "firstName",
    type : "string",
    validators : [ {
      type : "length",
      max : 10
    } ],
    allowBlank : false
  }, {
    name : "title",
    type : "string",
    validators : [ {
      type : "length",
      max : 30
    } ],
    allowBlank : true
  }, {
    name : "titleOfCourtesy",
    type : "string",
    validators : [ {
      type : "length",
      max : 25
    } ],
    allowBlank : true
  }, {
    name : "birthDate",
    type : "date",
    validators : [ {
      type : "length",
      max : 255
    } ],
    allowBlank : true
  }, {
    name : "hireDate",
    type : "date",
    validators : [ {
      type : "length",
      max : 255
    } ],
    allowBlank : true
  }, {
    name : "address",
    type : "string",
    validators : [ {
      type : "length",
      max : 60
    } ],
    allowBlank : true
  }, {
    name : "city",
    type : "string",
    validators : [ {
      type : "length",
      max : 15
    } ],
    allowBlank : true
  }, {
    name : "region",
    type : "string",
    validators : [ {
      type : "length",
      max : 15
    } ],
    allowBlank : true
  }, {
    name : "postalCode",
    type : "string",
    validators : [ {
      type : "length",
      max : 10
    } ],
    allowBlank : true
  }, {
    name : "country",
    type : "string",
    validators : [ {
      type : "length",
      max : 15
    } ],
    allowBlank : true
  }, {
    name : "homePhone",
    type : "string",
    validators : [ {
      type : "length",
      max : 24
    } ],
    allowBlank : true
  }, {
    name : "extension",
    type : "string",
    validators : [ {
      type : "length",
      max : 4
    } ],
    allowBlank : true
  }, {
    name : "notes",
    type : "string",
    validators : [ {
      type : "length",
      max : 255
    } ],
    allowBlank : true
  }, {
    name : "photoPath",
    type : "string",
    validators : [ {
      type : "length",
      max : 255
    } ],
    allowBlank : true
  }, {
    name : "salary",
    type : "number",
    validators : [ {
      type : "length",
      max : 255
    } ],
    allowBlank : true
  } ],
  associations : [ {
    type : "hasMany",
    model : "ch.rasc.extclassgenerator.bean.Employees",
    name : "reportsTo"
  } ]
});