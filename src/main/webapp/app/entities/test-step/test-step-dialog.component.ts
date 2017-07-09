import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TestStep } from './test-step.model';
import { TestStepPopupService } from './test-step-popup.service';
import { TestStepService } from './test-step.service';
import { TestCase, TestCaseService } from '../test-case';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-test-step-dialog',
    templateUrl: './test-step-dialog.component.html'
})
export class TestStepDialogComponent implements OnInit {

    testStep: TestStep;
    authorities: any[];
    isSaving: boolean;

    testcases: TestCase[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private testStepService: TestStepService,
        private testCaseService: TestCaseService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.testCaseService.query()
            .subscribe((res: ResponseWrapper) => { this.testcases = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.testStep.id !== undefined) {
            this.subscribeToSaveResponse(
                this.testStepService.update(this.testStep));
        } else {
            this.subscribeToSaveResponse(
                this.testStepService.create(this.testStep));
        }
    }

    private subscribeToSaveResponse(result: Observable<TestStep>) {
        result.subscribe((res: TestStep) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TestStep) {
        this.eventManager.broadcast({ name: 'testStepListModification', content: 'OK'});
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

    trackTestCaseById(index: number, item: TestCase) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-test-step-popup',
    template: ''
})
export class TestStepPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private testStepPopupService: TestStepPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.testStepPopupService
                    .open(TestStepDialogComponent, params['id']);
            } else {
                this.modalRef = this.testStepPopupService
                    .open(TestStepDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
