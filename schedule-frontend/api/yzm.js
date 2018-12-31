import { request } from '../utils/request';
import { domain } from './domain';

export function requestYzm (data = {}) {
  return request({
    url: `${domain}/yzm`,
    dataType: '其他',
    responseType: 'arraybuffer',
    ...data
  }) 
}