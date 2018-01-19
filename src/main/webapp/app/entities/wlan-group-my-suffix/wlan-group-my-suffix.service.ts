import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { WlanGroupMySuffix } from './wlan-group-my-suffix.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class WlanGroupMySuffixService {

    private resourceUrl =  SERVER_API_URL + 'api/wlan-groups';

    constructor(private http: Http) { }

    create(wlanGroup: WlanGroupMySuffix): Observable<WlanGroupMySuffix> {
        const copy = this.convert(wlanGroup);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(wlanGroup: WlanGroupMySuffix): Observable<WlanGroupMySuffix> {
        const copy = this.convert(wlanGroup);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<WlanGroupMySuffix> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to WlanGroupMySuffix.
     */
    private convertItemFromServer(json: any): WlanGroupMySuffix {
        const entity: WlanGroupMySuffix = Object.assign(new WlanGroupMySuffix(), json);
        return entity;
    }

    /**
     * Convert a WlanGroupMySuffix to a JSON which can be sent to the server.
     */
    private convert(wlanGroup: WlanGroupMySuffix): WlanGroupMySuffix {
        const copy: WlanGroupMySuffix = Object.assign({}, wlanGroup);
        return copy;
    }
}
