/**
 * Generated Time:Fri Jun 01 17:06:42 GMT+08:00 2018
 */
Ext.define("ReadWrite",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.proxy.Direct" ],
  fields : [ "id", "name" ],
  proxy : {
    type : "direct",
    reader : {
      type : "myreader"
    },
    writer : {
      type : "mywriter",
      writeAllFields : true
    }
  }
});