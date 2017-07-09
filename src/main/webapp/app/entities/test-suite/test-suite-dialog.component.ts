import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TestSuite } from './test-suite.model';
import { TestSuitePopupService } from './test-suite-popup.service';
import { TestSuiteService } from './test-suite.service';
import { Specification, SpecificationService } from '../specification';
import { MetaData, MetaDataService } from '../meta-data';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-test-suite-dialog',
    templateUrl: './test-suite-dialog.component.html'
})
export class TestSuiteDialogComponent implements OnInit {

    testSuite: TestSuite;
    authorities: any[];
    isSaving: boolean;

    specifications: Specification[];

    metadata: MetaData[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private testSuiteService: TestSuiteService,
        private specificationService: SpecificationService,
        private metaDataService: MetaDataService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.specificationService.query()
            .subscribe((res: ResponseWrapper) => { this.specifications = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.metaDataService
            .query({filter: 'testsuite-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.testSuite.metaDataId) {
                    this.metadata = res.json;
                } else {
                    this.metaDataService
                        .find(this.testSuite.metaDataId)
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
        if (this.testSuite.id !== undefined) {
            this.subscribeToSaveResponse(
                this.testSuiteService.update(this.testSuite));
        } else {
            this.subscribeToSaveResponse(
                this.testSuiteService.create(this.testSuite));
        }
    }

    private subscribeToSaveResponse(result: Observable<TestSuite>) {
        result.subscribe((res: TestSuite) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TestSuite) {
        this.eventManager.broadcast({ name: 'testSuiteListModification', content: 'OK'});
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

    trackSpecificationById(index: number, item: Specification) {
        return item.id;
    }

    trackMetaDataById(index: number, item: MetaData) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-test-suite-popup',
    template: ''
})
export class TestSuitePopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private testSuitePopupService: TestSuitePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.testSuitePopupService
                    .open(TestSuiteDialogComponent, params['id']);
            } else {
                this.modalRef = this.testSuitePopupService
                    .open(TestSuiteDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
