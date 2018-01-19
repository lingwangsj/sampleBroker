import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { WlanMySuffix } from './wlan-my-suffix.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class WlanMySuffixService {

    private resourceUrl =  SERVER_API_URL + 'api/wlans';

    constructor(private http: Http) { }

    create(wlan: WlanMySuffix): Observable<WlanMySuffix> {
        const copy = this.convert(wlan);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(wlan: WlanMySuffix): Observable<WlanMySuffix> {
        const copy = this.convert(wlan);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<WlanMySuffix> {
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
     * Convert a returned JSON object to WlanMySuffix.
     */
    private convertItemFromServer(json: any): WlanMySuffix {
        const entity: WlanMySuffix = Object.assign(new WlanMySuffix(), json);
        return entity;
    }

    /**
     * Convert a WlanMySuffix to a JSON which can be sent to the server.
     */
    private convert(wlan: WlanMySuffix): WlanMySuffix {
        const copy: WlanMySuffix = Object.assign({}, wlan);
        return copy;
    }
}
