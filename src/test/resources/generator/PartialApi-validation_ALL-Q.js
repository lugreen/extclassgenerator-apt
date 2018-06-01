/**
 * Generated Time:Fri Jun 01 17:08:26 GMT+08:00 2018
 */
Ext.define("App.PartialApi",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.proxy.Direct" ],
  fields : [ {
    name : "id",
    type : "integer"
  }, {
    name : "name",
    type : "string"
  } ],
  proxy : {
    type : "direct",
    api : {
      read : "read",
      destroy : "destroy"
    },
    writer : {
      writeAllFields : true
    }
  }
});