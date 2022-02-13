$(function () {
    var names = new Array();//商品名称
    var prices = new Array();//价格
    var index = 0;
    $.getJSON('../static/data/data.json', function (data) {
        for (var i in data.goods) {
            names[index] = data.goods[i].name;
            prices[index] = data.goods[i].price;
            index++;
        }
    });
    const itemIndex = 0;
    const pageSize = 1;//每页多少行
    var lineNum = 0;//起始行
    const lineSize = 5;//每行大小
    var isEnd = false
    // dropload
    var dropload = $('.khfxWarp').dropload({
        scrollArea: window,
        domDown: {
            domClass: 'dropload-down',
            domRefresh: '<div class="dropload-refresh">上拉加载更多</div>',
            domLoad: '<div class="dropload-load"><span class="loading"></span>加载中...</div>',
            domNoData: '<div class="dropload-noData">已无数据</div>'
        },
        loadDownFn: function (me) {
            setTimeout(function () {
                if (isEnd) {//无数据
                    me.resetload();
                    me.lock();
                    me.noData();
                    me.resetload();
                    return;
                }
                let result = '';
                for (let i = lineNum; i < lineNum + pageSize; i++) {//第几行
                    let numIndex = 0;
                    for (let j = 0; j < lineSize; j++) {//每行的第几个
                        numIndex = i * lineSize + j + 1;
                        if (numIndex > 19) {
                            isEnd = true
                            break
                        }
                        result
                            += ''
                            + '    <hgroup class="khfxRow">'
                            + '      <div class="col-xs-1-5">'
                            + '<a href="#" class="thumbnail" style="text-decoration: none">'
                            + '                       <div><img src="../static/image/recommend/' + numIndex + '.png" alt="..." class="img-responsive center-block"'
                            + '                                 style="width:300px; height:200px;"></div>                                          '
                            + '                        <div style="height: 49px;text-overflow: -o-ellipsis-lastline;overflow: hidden;text-overflow: ellipsis;display: -webkit-box;' +
                            '-webkit-line-clamp: 2;line-clamp: 2;-webkit-box-orient: vertical; "><h4 style="color: black;">' + names[numIndex - 1] + '</h4></div>                                   '
                            + '                        <div><h5 style="color: red;display: inline">¥&nbsp;</h5><h3 style="color: red;display: inline">' + prices[numIndex - 1] + '</h3></div>                                   '
                            + '            </a>                                                                                                   '
                            + '      </div>'
                            + '    </hgroup>';

                    }

                    if (numIndex >= 19) {
                        break
                    }
                }
                lineNum++ //下一行

                $('.khfxPane').eq(itemIndex).append(result);
                me.resetload();
            }, 0);
            // }
        }
    });

});