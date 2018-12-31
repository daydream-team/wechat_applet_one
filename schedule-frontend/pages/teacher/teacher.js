import { requestSessionId } from '../../api/sessionId';
import { requestYzm } from '../../api/yzm';
import { requestSource } from '../../api/source';

Page({
  sessionId: '',
  name: '',
  zjhm: '',
  yzm: '',
  data: {
    imageBase64: '',
  },
  onLoad() {
    requestSessionId().then(res => {
      this.sessionId = res.data;

      if (this.sessionId) {
        this.getYzm(this.sessionId);
      }
    }).catch(err => {
      console.error(err);
    });
  },
  getYzm: function(sessionId) {
    requestYzm({
      data: {
        sessionId
      }
    }).then(res => {
        if (res.data) {
          this.setData({
            imageBase64: `data:image/jpg;base64, ${wx.arrayBufferToBase64(res.data)}`
          });
        }
    }).catch(err => {
      console.error(err);
    });
  },
  searchResult() {
    requestSource({
      data: {
        name: this.name,
        zjhm: this.zjhm,
        yzm: this.yzm,
        sessionId: this.sessionId
      },
    }).then(res => {
      console.log(res);
    }).catch(err => {
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
  }
})