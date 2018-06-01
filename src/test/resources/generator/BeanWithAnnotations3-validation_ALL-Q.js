/**
 * Generated Time:Fri Jun 01 17:07:20 GMT+08:00 2018
 */
Ext.define("Sch.Bean3",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.proxy.Direct" ],
  fields : [ {
    name : "id",
    type : "string"
  }, {
    name : "name",
    type : "string"
  } ],
  proxy : {
    type : "direct",
    directFn : "read",
    reader : {
      rootProperty : "theRootProperty",
      messageProperty : "theMessageProperty",
      totalProperty : "theTotalProperty",
      successProperty : "theSuccessProperty"
    },
    writer : {
      writeAllFields : true
    }
  }
});