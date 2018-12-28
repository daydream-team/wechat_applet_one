//index.js
//获取应用实例
const app = getApp()

Page({
  teacher_index:function() {
    wx.switchTab({
      url: '../teacher/teacher',
    })
  },
  cet_index: function () {
    wx.navigateTo({
      url: '../cet/cet',
    })
  },
  mandarin_index: function () {
    wx.navigateTo({
      url: '../mandarin/mandarin',
    })
  },
  vfp_index: function () {
    wx.navigateTo({
      url: '../vfp/vfp',
    })
  }
})
