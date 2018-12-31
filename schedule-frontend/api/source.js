import { request } from '../utils/request';
import { domain } from './domain';

export function requestSource (data = {}) {
  return request({
    url: `${domain}/source`,
    method: 'POST',
    ...data
  }) 
}