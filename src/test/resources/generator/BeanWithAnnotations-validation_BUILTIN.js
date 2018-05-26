Ext.define("Sch.Bean",
{
  extend : "Ext.data.Model",
  requires : [ "Ext.data.proxy.Direct", "Ext.data.validator.Email", "Ext.data.validator.Format", "Ext.data.validator.Length", "Ext.data.validator.Presence", "Ext.data.validator.Range" ],
  idProperty : "aInt",
  fields : [ {
    name : "by",
    type : "integer"
  }, {
    name : "aShort",
    type : "integer"
  }, {
    name : "aInt",
    type : "integer"
  }, {
    name : "lo",
    type : "integer",
    defaultValue : -1
  }, {
    name : "aByteObject",
    type : "string"
  }, {
    name : "aShortObject",
    type : "number"
  }, {
    name : "aIntObject",
    type : "integer"
  }, {
    name : "aLongObject",
    type : "integer",
    allowNull : true
  }, {
    name : "aBigDecimal",
    type : "number"
  }, {
    name : "aBigInteger",
    type : "integer",
    defaultValue : 1,
    validators : [ {
      type : "presence"
    }, {
      type : "range",
      max : 500000
    } ]
  }, {
    name : "aFloat",
    type : "number",
    defaultValue : 1.1
  }, {
    name : "aDouble",
    type : "number",
    validators : [ {
      type : "presence"
    } ]
  }, {
    name : "aFloatObject",
    type : "number"
  }, {
    name : "aDoubleObject",
    type : "number"
  }, {
    name : "aString",
    type : "string",
    allowNull : true,
    validators : [ {
      type : "email"
    }, {
      type : "length",
      min : 0,
      max : 255
    }, {
      type : "format",
      matcher : /\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\]/
    } ]
  }, {
    name : "aBoolean",
    type : "boolean",
    defaultValue : true
  }, {
    name : "aBooleanObject",
    type : "boolean",
    defaultValue : false
  }, {
    name : "aDate",
    type : "date",
    dateFormat : "c"
  }, {
    name : "aSqlDate",
    type : "date"
  }, {
    name : "aTimestamp",
    type : "date"
  }, {
    name : "aDateTime",
    type : "date",
    dateFormat : "d.m.y"
  }, {
    name : "aLocalDate",
    type : "date"
  }, {
    name : "bigValue",
    type : "integer"
  }, {
    name : "aNonCriticalValue",
    type : "string"
  }, {
    name : "aCriticalValue",
    type : "string",
    critical : true
  }, {
    name : "aBooleanVirtual",
    type : "boolean",
    persist : false,
    mapping : "bigValue",
    convert : function(v, record) { return (record.raw.bigValue > 1000000);},
    depends : [ "lastName", "firstName" ]
  }, {
    name : "calculatedValue",
    type : "string",
    calculate : function(data) { return 'CALC:' + data.aString; }
  }, "someIds", "moreIds" ],
  proxy : {
    type : "direct",
    idParam : "aInt",
    api : {
      read : read,
      create : create,
      update : update,
      destroy : destroy
    },
    reader : {
      rootProperty : "records"
    },
    writer : {
      writeAllFields : true
    }
  }
});