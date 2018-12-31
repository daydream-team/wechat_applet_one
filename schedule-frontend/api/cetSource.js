import { request } from '../utils/request';
import { domain } from './domain';

export function requestCetSource (data = {}) {
  return request({
    url: `${domain}/cetSource`,
    method: 'POST',
    ...data
  }) 
}