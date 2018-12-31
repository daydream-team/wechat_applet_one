import { request } from '../utils/request';
import { domain } from './domain';

export function requestSessionId (data) {
  return request({
    url: `${domain}/sessionId`,
    ...data
  }) 
}