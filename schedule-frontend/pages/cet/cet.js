import {
  requestCetYzm
} from '../../api/cetYzm';
import {
  requestCetSource
} from '../../api/cetSource';

Page({
  id: '',
  yzm: '',
  name: '',
  timestamp: 0,
  key: '',
  data: {
    imgBase64: '',
    result: null,
    info: null
  },
  focusYzm() {
    if (!this.id) {
      wx.showToast({
        title: '输入不能为空',
        icon: 'none'
      });
      return;
    }

    requestCetYzm({
      data: {
        IdCard: this.id
      }
    }).then(res => {
      if (res.data) {
        this.setData({
          imgBase64: res.data.imgBase64
        });
        this.timestamp = res.data.timestamp;
        this.key = res.data.key;
      }
    }).catch(err => {
      wx.showModal({
        title: '提示',
        content: '准考证号码输入有误，请重新输入',
        showCancel: false
      })
      console.error(err);
    })
  },
  searchResult() {
    if (!this.validInput) {
      wx.showToast({
        title: '输入不能为空',
        icon: 'none'
      });
      return;
    }

    wx.showToast({
      title: '查询中',
      icon: 'loading'
    })
    requestCetSource({
      data: {
        timestamp: this.timestamp,
        key: this.key,
        IdCard: this.id,
        name: this.name,
        yzm: this.yzm,
      }
    }).then(res => {
      wx.hideToast();

      if (res.data && res.data.code !== 'ERROR') {
        this.setData({
          result: res.data,
          info: {
            name: this.name,
            id: this.id
          }
        });
      } else {
        wx.showModal({
          title: '提示',
          content: '输入信息有误，请修改后重新查询',
          showCancel: false
        })
      }
    }).catch(err => {
      wx.hideToast();
      wx.showModal({
        title: '提示',
        content: '系统异常，请稍后再试',
        showCancel: false
      })
      console.error(err);
    });
  },
  inputId(e) {
    this.id = e.detail.value
  },
  inputYzm(e) {
    this.yzm = e.detail.value
  },
  inputName(e) {
    this.name = e.detail.value
  },
  validInput() {
    return [
      this.timestamp,
      this.key,
      this.id,
      this.cet,
      this.name,
      this.yzm
    ].every(v => !!v)
  }
})