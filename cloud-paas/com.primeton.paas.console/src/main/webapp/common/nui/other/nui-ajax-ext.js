
nui.DataTree.prototype._doLoadAjax = nui.DataSource.prototype._doLoadAjax = nui.DataTable.prototype._doLoadAjax = function (params, success, error, complete, _successHandler) {
		
        params = params || {};
        if (mini.isNull(params.pageIndex)) params.pageIndex = this.pageIndex;
        if (mini.isNull(params.pageSize)) params.pageSize = this.pageSize;

        if (params.sortField) this.sortField = params.sortField;
        if (params.sortOrder) this.sortOrder = params.sortOrder;
        params.sortField = this.sortField;
        params.sortOrder = this.sortOrder;

        this.loadParams = params;

        var url = this._evalUrl();
        var type = this._evalType(url);

        var obj = mini._evalAjaxData(this.ajaxData, this);
        mini.copyTo(params, obj);

        var e = {
            url: url,
            async: this.ajaxAsync,
            type: type,
            data: params,
            params: params,
            cache: false,
            cancel: false
        };

        //ajaxOptions：async, type, dateType, contentType等都能在beforeload前修改。                

        mini.copyTo(e, this.ajaxOptions);

        this._OnBeforeLoad(e);
        if (e.cancel == true) {
            params.pageIndex = this.getPageIndex();
            params.pageSize = this.getPageSize();
            return;
        }

        //历史遗留问题：兼容e.params参数
        if (e.data != e.params && e.params != params) {
            e.data = e.params;
        }

        if (e.url != url && e.type == type) {
            e.type = this._evalType(e.url);
        }

        //处理自定义field
        var o = {};
        o[this.pageIndexField] = params.pageIndex;
        o[this.pageSizeField] = params.pageSize;
        if (params.sortField) o[this.sortFieldField] = params.sortField;
        if (params.sortOrder) o[this.sortOrderField] = params.sortOrder;

        if (this.startField && this.limitField) {
            o[this.startField] = params.pageIndex * params.pageSize;
            o[this.limitField] = params.pageSize;
        }

        //        delete params.pageIndex;
        //        delete params.pageSize;
        //        delete params.sortField;
        //        delete params.sortOrder;
        mini.copyTo(params, o);


        if (this.sortMode == 'client') {
            params[this.sortFieldField] = "";
            params[this.sortOrderField] = "";
        }

        //保存记录值
        var selected = this.getSelected();
        this._selectedValue = selected ? selected[this.idField] : null;
        if (mini.isNumber(this._selectedValue)) this._selectedValue = String(this._selectedValue);

        var me = this;
        me._resultObject = null;
        /*
        e.textStatus
        success     交互成功
        error       网络交互失败：404,500
        timeout     交互超时
        abort       交互终止
        servererror 网络交互成功，返回json，但是业务逻辑错误
        e.errorCode     服务端错误码
        e.errorMsg      错误描述信息
        e.stackTrace    错误定位信息
        */
        
        var async = e.async;
        mini.copyTo(e, {
            success: function (text, textStatus, xhr) {
                if (!text || text == "null") {
                    text = '{ tatal: 0, data: [] }';
                }
                
                delete e.params;
                var obj = { text: text, result: null, sender: me, options: e, xhr: xhr };
                var result = null;
                try {
                    //mini_doload(obj);
                    result = obj.result;
                    if (!result) {
                        result = mini.decode(text);
                    }
                } catch (ex) {
                    if (mini_debugger == true) {
                        alert(url + "\n json is error.");
                    }
                }

                if (result && !mini.isArray(result)) {
                    result.total = parseInt(mini._getMap(me.totalField, result)); //result[me.totalField];
                    result.data = mini._getMap(me.dataField, result); //result[me.dataField];
                } else {
                    if (result == null) {
                        result = {};
                        result.data = [];
                        result.total = 0;
                    } else if (mini.isArray(result)) {
                        var r = {};
                        r.data = result;
                        r.total = result.length;
                        result = r;
                    }
                }
                if (!result.data) result.data = [];
                if (!result.total) result.total = 0;
                me._resultObject = result;

                if (!mini.isArray(result.data)) {
                    result.data = [result.data];
                }

                var ex = {
                    xhr: xhr,
                    text: text,
                    textStatus: textStatus,
                    result: result,
                    total: result.total,
                    data: result.data.clone(),

                    pageIndex: params[me.pageIndexField],
                    pageSize: params[me.pageSizeField]
                };


                var error = mini._getMap(me.errorField, result);
                var errorMsg = mini._getMap(me.errorMsgField, result);
                var stackTrace = mini._getMap(me.stackTraceField, result);

                if (mini.isNumber(error) && error != 0 || error === false) {
                    //server error
                    ex.textStatus = "servererror";
                    ex.errorCode = error;
                    ex.stackTrace = stackTrace || "";
                    ex.errorMsg = errorMsg || "";
                    if (mini_debugger == true) {
                        alert(url + "\n" + ex.textStatus + "\n" + ex.errorMsg + "\n" + ex.stackTrace);
                    }
                    me.fire("loaderror", ex);
                    if (error) error.call(me, ex);
                } else {

                    if (_successHandler) {
                        _successHandler(ex);
                    } else {
                        //pager
                        me.pageIndex = ex.pageIndex;
                        me.pageSize = ex.pageSize;
                        me.setTotalCount(ex.total);

                        //success
                        me._OnPreLoad(ex);

                        //data
                        me.setData(ex.data);

                        //checkSelectOnLoad
                        if (me._selectedValue && me.checkSelectOnLoad) {
                            var o = me.getbyId(me._selectedValue);
                            if (o) {
                                me.select(o);
                            }
                        }
                        //selectOnLoad                    
                        if (me.getSelected() == null && me.selectOnLoad && me.getDataView().length > 0) {
                            me.select(0);
                        }
                        me.fire("load", ex);

                        if (success) {
                            if (async) {
                                setTimeout(function () {
                                    success.call(me, ex);
                                }, 20);
                            } else {
                                success.call(me, ex);
                            }
                        }
                    }
                }
            },
            error: function (xhr, textStatus, errorThrown) {
                if (textStatus == "abort") return;

                var ex = {
                    xhr: xhr,
                    text: xhr.responseText,
                    textStatus: textStatus
                };
                ex.errorMsg = xhr.responseText;
                ex.errorCode = xhr.status;


                if (mini_debugger == true) {
                    //alert(url + "\n" + ex.errorCode + "\n" + ex.errorMsg);
                }

                me.fire("loaderror", ex);
                if (error) error.call(me, ex);
            },
            complete: function (xhr, textStatus) {
                var ex = {
                    xhr: xhr,
                    text: xhr.responseText,
                    textStatus: textStatus
                };
                me.fire("loadcomplete", ex);
                if (complete) complete.call(me, ex);
                me._xhr = null;
            }
        });
        if (this._xhr) {
            //this._xhr.abort();
        }
        this._xhr = mini.ajax(e);
};

nui.ListBox.prototype._doLoad = function (params) {

        try {
            var url = eval(this.url);
            if (url != undefined) {
                this.url = url;
            }
        } catch (e) { }
        var url = this.url;

        var ajaxMethod = mini.ListControl.ajaxType;
        if (url) {
            if (url.indexOf(".txt") != -1 || url.indexOf(".json") != -1) {
                ajaxMethod = "get";
            }
        }

        var obj = mini._evalAjaxData(this.ajaxData, this);
        mini.copyTo(params, obj);

        var e = {
            url: this.url,
            async: false,
            type: this.ajaxType ? this.ajaxType : ajaxMethod,
            data: params,
            params: params,
            cache: false,
            cancel: false
        };
        this.fire("beforeload", e);
        if (e.data != e.params && e.params != params) {
            e.data = e.params;
        }
        if (e.cancel == true) return;

        var sf = me = this;
        var url = e.url;
        mini.copyTo(e, {
            success: function (text, textStatus, xhr) {
                delete e.params;
                var obj = { text: text, result: null, sender: me, options: e, xhr: xhr };
                var result = null;
                try {
                    //mini_doload(obj);
                    result = obj.result;
                    if (!result) {
                        result = mini.decode(text);
                    }
                } catch (ex) {
                    if (mini_debugger == true) {
                        alert(url + "\njson is error.");
                    }
                }
                if (mini.isArray(result)) result = { data: result };
                if (sf.dataField) {
                    result.data = mini._getMap(sf.dataField, result);
                }
                if (!result.data) result.data = [];

                var ex = { data: result.data, cancel: false };
                sf.fire("preload", ex);
                if (ex.cancel == true) return;

                sf.setData(ex.data);

                sf.fire("load");

                setTimeout(function () {
                    sf.doLayout();
                }, 100);

            },
            error: function (xhr, textStatus, errorThrown) {
                var e = {
                    xhr: xhr,
                    text: xhr.responseText,
                    textStatus: textStatus,
                    errorMsg: xhr.responseText,
                    errorCode: xhr.status
                };
                sf.fire("loaderror", e);
            }
        });

        this._ajaxer = mini.ajax(e);
};