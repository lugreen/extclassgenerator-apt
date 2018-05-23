Ext.define("Sch.Bean2",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.proxy.Direct", "Ext.data.validator.Format" ],
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
  } ],
  proxy : {
    type : "direct",
    pageParam : "",
    startParam : "",
    limitParam : "",
    directFn : read,
    reader : {
      messageProperty : "theMessageProperty"
    },
    writer : {
      writeAllFields : true
    }
  }
});