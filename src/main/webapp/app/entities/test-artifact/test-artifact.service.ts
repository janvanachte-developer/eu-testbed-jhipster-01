import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { TestArtifact } from './test-artifact.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class TestArtifactService {

    private resourceUrl = 'api/test-artifacts';
    private resourceSearchUrl = 'api/_search/test-artifacts';

    constructor(private http: Http) { }

    create(testArtifact: TestArtifact): Observable<TestArtifact> {
        const copy = this.convert(testArtifact);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(testArtifact: TestArtifact): Observable<TestArtifact> {
        const copy = this.convert(testArtifact);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<TestArtifact> {
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

    private convert(testArtifact: TestArtifact): TestArtifact {
        const copy: TestArtifact = Object.assign({}, testArtifact);
        return copy;
    }
}
