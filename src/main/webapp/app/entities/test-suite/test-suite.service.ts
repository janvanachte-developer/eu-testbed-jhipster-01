import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { TestSuite } from './test-suite.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TestSuiteService {

    private resourceUrl = 'api/test-suites';
    private resourceSearchUrl = 'api/_search/test-suites';

    constructor(private http: Http) { }

    create(testSuite: TestSuite): Observable<TestSuite> {
        const copy = this.convert(testSuite);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(testSuite: TestSuite): Observable<TestSuite> {
        const copy = this.convert(testSuite);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<TestSuite> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
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

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(testSuite: TestSuite): TestSuite {
        const copy: TestSuite = Object.assign({}, testSuite);
        return copy;
    }
}
