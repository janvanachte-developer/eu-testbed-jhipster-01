import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TestCase } from './test-case.model';
import { TestCasePopupService } from './test-case-popup.service';
import { TestCaseService } from './test-case.service';
import { TestSuite, TestSuiteService } from '../test-suite';
import { MetaData, MetaDataService } from '../meta-data';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-test-case-dialog',
    templateUrl: './test-case-dialog.component.html'
})
export class TestCaseDialogComponent implements OnInit {

    testCase: TestCase;
    authorities: any[];
    isSaving: boolean;

    testsuites: TestSuite[];

    metadata: MetaData[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private testCaseService: TestCaseService,
        private testSuiteService: TestSuiteService,
        private metaDataService: MetaDataService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.testSuiteService.query()
            .subscribe((res: ResponseWrapper) => { this.testsuites = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.metaDataService
            .query({filter: 'testcase-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.testCase.metaDataId) {
                    this.metadata = res.json;
                } else {
                    this.metaDataService
                        .find(this.testCase.metaDataId)
                        .subscribe((subRes: MetaData) => {
                            this.metadata = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.testCase.id !== undefined) {
            this.subscribeToSaveResponse(
                this.testCaseService.update(this.testCase));
        } else {
            this.subscribeToSaveResponse(
                this.testCaseService.create(this.testCase));
        }
    }

    private subscribeToSaveResponse(result: Observable<TestCase>) {
        result.subscribe((res: TestCase) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TestCase) {
        this.eventManager.broadcast({ name: 'testCaseListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackTestSuiteById(index: number, item: TestSuite) {
        return item.id;
    }

    trackMetaDataById(index: number, item: MetaData) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-test-case-popup',
    template: ''
})
export class TestCasePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private testCasePopupService: TestCasePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.testCasePopupService
                    .open(TestCaseDialogComponent, params['id']);
            } else {
                this.modalRef = this.testCasePopupService
                    .open(TestCaseDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
