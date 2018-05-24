Ext.define("Sch.BeanWithCustomType",
{
  extend : "Ext.data.Model",
  fields : [ {
    name : "age",
    type : "integer"
  }, {
    name : "city",
    type : "city"
  }, {
    name : "email",
    type : "email"
  }, {
    name : "creditcardnumber",
    type : "creditcardnumber"
  } ]
});