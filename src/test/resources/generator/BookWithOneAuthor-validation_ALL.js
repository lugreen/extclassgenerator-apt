/**
 * Generated Time:Fri Jun 01 17:06:42 GMT+08:00 2018
 */
Ext.define("ch.rasc.extclassgenerator.bean.BookWithOneAuthor",
{
  extend : "Ext.data.Model",
  uses : [ "MyApp.Author" ],
  fields : [ {
    name : "id",
    type : "integer"
  }, {
    name : "author_id",
    type : "integer"
  } ],
  associations : [ {
    type : "hasOne",
    model : "MyApp.Author",
    associationKey : "author",
    foreignKey : "author_id",
    setterName : "setAuthor",
    getterName : "getAuthor"
  } ]
});