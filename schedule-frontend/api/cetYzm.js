import { request } from '../utils/request';
import { domain } from './domain';

export function requestCetYzm (data) {
  return request({
    url: `${domain}/cetYzm`,
    ...data
  }) 
}