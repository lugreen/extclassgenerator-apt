Ext.define("Sch.Bean2",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.proxy.Direct", "Ext.data.validator.Format", "Ext.data.validator.Length" ],
  fields : [ {
    name : "id",
    type : "integer"
  }, {
    name : "name",
    type : "string",
    validators : [ {
      type : "format",
      matcher : /[a-zA-Z]*/
    } ]
  }, {
    name : "dob",
    type : "date",
    dateFormat : "c"
  }, {
    name : "accountNo",
    type : "string",
    validators : [ {
      type : "length",
      min : 2,
      max : 10
    } ]
  } ],
  proxy : {
    type : "direct",
    directFn : read,
    reader : {
      messageProperty : "theMessageProperty"
    },
    writer : {
      writeAllFields : true
    }
  }
});