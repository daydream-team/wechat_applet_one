// pages/teacher/teacher.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    apply_left:"剩余天数:30",
    write_left: "剩余天数:40",
    interview_left: "剩余天数:50",
    apply_percent: 70,
    write_percent: 60,
    interview_percent: 50,
    sessionId : "",
    image: "",
    name: "",
    zjhm:"",
    yzm:""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    console.log(getApp().globalData.yzmUrl);
    var app = getApp().globalData;
    var sessionIdUrl = app.sessionIdUrl;
    var yzmUrl = app.yzmUrl;
    var sourceUrl = app.sourceUrl;
    var that = this;
    // 1.获取sessionId
    wx.request({
      url: sessionIdUrl,
      method: 'GET',
      success(result) {
        that.setData({ sessionId: result.data });
        // 2.获取验证码
        that.requireYzm();
      }
    });
    
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {
    
  },
  requireYzm: function() {
    var app = getApp().globalData;
    var yzmUrl = app.yzmUrl;
    var that = this;
    wx.request({
      url: yzmUrl,
      data: {
        sessionId: that.data.sessionId
      },
      method: 'GET',
      dataType: '其他',
      responseType: 'arraybuffer',
      success: function (res) {
        var image = wx.arrayBufferToBase64(res.data);
        console.log(image);
        that.setData({ image: 'data:image/jpg;base64,' + image });
      },
      fail: function (res) { },
      complete: function (res) {
        console.log(res);
      },
    })
  },
  handleClick: function (e) {
    console.log(this.data.name);
    console.log(this.data.zjhm);
    console.log(this.data.yzm);
    var name = this.data.name;
    var zjhm = this.data.zjhm;
    var yzm = this.data.yzm;
    var sessionId = this.data.sessionId;
    var app = getApp().globalData;
    var sourceUrl = app.sourceUrl;
    console.log(sessionId);
    console.log(sourceUrl)

    wx.request({
      url: sourceUrl,
      method: 'POST',
      data: {
        name: name,
        zjhm: zjhm,
        yzm:yzm,
        sessionId: sessionId
      },
      complete: function(result) {
        console.log(result);
      },success: function(result) {
        console.log(result);
      }

    })
  },
  /*获取名称***/
  nameInput: function (e) {
    this.setData({
      name: e.detail.detail.value
    })
  },
  /*获取证件号码***/
  zjhmInput: function (e) {
    this.setData({
      zjhm: e.detail.detail.value
    })
  },
  /*获取验证码***/
  yzmInput: function (e) {
    this.setData({
      yzm: e.detail.detail.value
    })
  }

})