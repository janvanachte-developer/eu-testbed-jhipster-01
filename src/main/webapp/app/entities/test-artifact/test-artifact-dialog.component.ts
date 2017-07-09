import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { TestArtifact } from './test-artifact.model';
import { TestArtifactPopupService } from './test-artifact-popup.service';
import { TestArtifactService } from './test-artifact.service';
import { TestStep, TestStepService } from '../test-step';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-test-artifact-dialog',
    templateUrl: './test-artifact-dialog.component.html'
})
export class TestArtifactDialogComponent implements OnInit {

    testArtifact: TestArtifact;
    authorities: any[];
    isSaving: boolean;

    teststeps: TestStep[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private alertService: JhiAlertService,
        private testArtifactService: TestArtifactService,
        private testStepService: TestStepService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.testStepService.query()
            .subscribe((res: ResponseWrapper) => { this.teststeps = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, testArtifact, field, isImage) {
        if (event && event.target.files && event.target.files[0]) {
            const file = event.target.files[0];
            if (isImage && !/^image\//.test(file.type)) {
                return;
            }
            this.dataUtils.toBase64(file, (base64Data) => {
                testArtifact[field] = base64Data;
                testArtifact[`${field}ContentType`] = file.type;
            });
        }
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.testArtifact.id !== undefined) {
            this.subscribeToSaveResponse(
                this.testArtifactService.update(this.testArtifact));
        } else {
            this.subscribeToSaveResponse(
                this.testArtifactService.create(this.testArtifact));
        }
    }

    private subscribeToSaveResponse(result: Observable<TestArtifact>) {
        result.subscribe((res: TestArtifact) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: TestArtifact) {
        this.eventManager.broadcast({ name: 'testArtifactListModification', content: 'OK'});
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

    trackTestStepById(index: number, item: TestStep) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-test-artifact-popup',
    template: ''
})
export class TestArtifactPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private testArtifactPopupService: TestArtifactPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.testArtifactPopupService
                    .open(TestArtifactDialogComponent, params['id']);
            } else {
                this.modalRef = this.testArtifactPopupService
                    .open(TestArtifactDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
