Ext.define("ch.rasc.extclassgenerator.bean.Employees",
{
  extend : "Ext.data.Model",
  fields : [ {
    name : "employeeId",
    type : "integer"
  }, {
    name : "lastName",
    type : "string"
  }, {
    name : "firstName",
    type : "string"
  }, {
    name : "title",
    type : "string"
  }, {
    name : "titleOfCourtesy",
    type : "string"
  }, {
    name : "birthDate",
    type : "date"
  }, {
    name : "hireDate",
    type : "date"
  }, {
    name : "address",
    type : "string"
  }, {
    name : "city",
    type : "string"
  }, {
    name : "region",
    type : "string"
  }, {
    name : "postalCode",
    type : "string"
  }, {
    name : "country",
    type : "string"
  }, {
    name : "homePhone",
    type : "string"
  }, {
    name : "extension",
    type : "string"
  }, {
    name : "notes",
    type : "string"
  }, {
    name : "photoPath",
    type : "string"
  }, {
    name : "salary",
    type : "number"
  } ]
});