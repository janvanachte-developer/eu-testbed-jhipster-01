import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { MetaData } from './meta-data.model';
import { MetaDataPopupService } from './meta-data-popup.service';
import { MetaDataService } from './meta-data.service';

@Component({
    selector: 'jhi-meta-data-dialog',
    templateUrl: './meta-data-dialog.component.html'
})
export class MetaDataDialogComponent implements OnInit {

    metaData: MetaData;
    authorities: any[];
    isSaving: boolean;

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private metaDataService: MetaDataService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.metaData.id !== undefined) {
            this.subscribeToSaveResponse(
                this.metaDataService.update(this.metaData));
        } else {
            this.subscribeToSaveResponse(
                this.metaDataService.create(this.metaData));
        }
    }

    private subscribeToSaveResponse(result: Observable<MetaData>) {
        result.subscribe((res: MetaData) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: MetaData) {
        this.eventManager.broadcast({ name: 'metaDataListModification', content: 'OK'});
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
}

@Component({
    selector: 'jhi-meta-data-popup',
    template: ''
})
export class MetaDataPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private metaDataPopupService: MetaDataPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.metaDataPopupService
                    .open(MetaDataDialogComponent, params['id']);
            } else {
                this.modalRef = this.metaDataPopupService
                    .open(MetaDataDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
