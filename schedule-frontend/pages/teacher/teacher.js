import {
  requestSessionId
} from '../../api/sessionId';
import {
  requestYzm
} from '../../api/yzm';
import {
  requestSource
} from '../../api/source';

Page({
  sessionId: '',
  name: '',
  zjhm: '',
  yzm: '',
  data: {
    imageBase64: '',
    result: null
  },
  onLoad() {
    requestSessionId().then(res => {
      console.log('sessionid res', res)
      this.sessionId = res.data;

      if (this.sessionId) {
        this.getYzm(this.sessionId);
      } else {
        // TODO:
      }
    }).catch(err => {
      console.error(err);
    });
  },
  getYzm(sessionId) {
    requestYzm({
      data: {
        sessionId
      }
    }).then(res => {
      console.log('yzm res', res)
      if (res.data) {
        this.setData({
          imageBase64: `data:image/jpg;base64, ${wx.arrayBufferToBase64(res.data)}`
        });
      }
    }).catch(err => {
      console.error(err);
    });
  },
  updateYzm() {
    this.getYzm(this.sessionId);
  },
  searchResult() {
    if (!this.validInput()) {
      wx.showToast({
        title: '输入不能为空',
        icon: 'none'
      })
      return;
    }
    wx.showToast({
      title: '查询中',
      icon: 'loading'
    })
    requestSource({
      data: {
        name: this.name,
        zjhm: this.zjhm,
        yzm: this.yzm,
        sessionId: this.sessionId
      },
    }).then(res => {
      console.log('result res', res);
      wx.hideToast();

      if (res.data && res.data.code !== 'ERROR') {
        this.setData({
          result: res.data
        });
      } else {
        wx.showModal({
          title: '提示',
          content: '输入有误，请修改后重新查询',
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
    })
  },
  inputName(e) {
    this.name = e.detail.value
  },
  inputZjhm(e) {
    this.zjhm = e.detail.value
  },
  inputYzm(e) {
    this.yzm = e.detail.value
  },
  validInput() {
    return [this.name, this.zjhm, this.yzm].every(v => !!v);
  }
})