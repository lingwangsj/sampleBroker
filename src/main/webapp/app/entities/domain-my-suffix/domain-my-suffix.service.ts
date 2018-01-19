import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { DomainMySuffix } from './domain-my-suffix.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class DomainMySuffixService {

    private resourceUrl =  SERVER_API_URL + 'api/domains';

    constructor(private http: Http) { }

    create(domain: DomainMySuffix): Observable<DomainMySuffix> {
        const copy = this.convert(domain);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(domain: DomainMySuffix): Observable<DomainMySuffix> {
        const copy = this.convert(domain);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<DomainMySuffix> {
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
     * Convert a returned JSON object to DomainMySuffix.
     */
    private convertItemFromServer(json: any): DomainMySuffix {
        const entity: DomainMySuffix = Object.assign(new DomainMySuffix(), json);
        return entity;
    }

    /**
     * Convert a DomainMySuffix to a JSON which can be sent to the server.
     */
    private convert(domain: DomainMySuffix): DomainMySuffix {
        const copy: DomainMySuffix = Object.assign({}, domain);
        return copy;
    }
}
