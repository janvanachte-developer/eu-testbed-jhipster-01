import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Domain } from './domain.model';
import { DomainPopupService } from './domain-popup.service';
import { DomainService } from './domain.service';
import { MetaData, MetaDataService } from '../meta-data';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-domain-dialog',
    templateUrl: './domain-dialog.component.html'
})
export class DomainDialogComponent implements OnInit {

    domain: Domain;
    authorities: any[];
    isSaving: boolean;

    metadata: MetaData[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private domainService: DomainService,
        private metaDataService: MetaDataService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.metaDataService
            .query({filter: 'domain-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.domain.metaDataId) {
                    this.metadata = res.json;
                } else {
                    this.metaDataService
                        .find(this.domain.metaDataId)
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
        if (this.domain.id !== undefined) {
            this.subscribeToSaveResponse(
                this.domainService.update(this.domain));
        } else {
            this.subscribeToSaveResponse(
                this.domainService.create(this.domain));
        }
    }

    private subscribeToSaveResponse(result: Observable<Domain>) {
        result.subscribe((res: Domain) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Domain) {
        this.eventManager.broadcast({ name: 'domainListModification', content: 'OK'});
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

    trackMetaDataById(index: number, item: MetaData) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-domain-popup',
    template: ''
})
export class DomainPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private domainPopupService: DomainPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.domainPopupService
                    .open(DomainDialogComponent, params['id']);
            } else {
                this.modalRef = this.domainPopupService
                    .open(DomainDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
