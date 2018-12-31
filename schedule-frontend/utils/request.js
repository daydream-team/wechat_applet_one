export function request (reqParams) {
  const defaultReqParams = {
      method: 'GET',
  }

  return new Promise((resolve, reject) => {
    wx.request({
      ...defaultReqParams,
      ...reqParams,
      success: resolve,
      fail: reject
    })
  });
}