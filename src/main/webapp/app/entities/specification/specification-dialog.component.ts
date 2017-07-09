import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Specification } from './specification.model';
import { SpecificationPopupService } from './specification-popup.service';
import { SpecificationService } from './specification.service';
import { Domain, DomainService } from '../domain';
import { MetaData, MetaDataService } from '../meta-data';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-specification-dialog',
    templateUrl: './specification-dialog.component.html'
})
export class SpecificationDialogComponent implements OnInit {

    specification: Specification;
    authorities: any[];
    isSaving: boolean;

    domains: Domain[];

    metadata: MetaData[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private specificationService: SpecificationService,
        private domainService: DomainService,
        private metaDataService: MetaDataService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        this.domainService.query()
            .subscribe((res: ResponseWrapper) => { this.domains = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.metaDataService
            .query({filter: 'specification-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.specification.metaDataId) {
                    this.metadata = res.json;
                } else {
                    this.metaDataService
                        .find(this.specification.metaDataId)
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
        if (this.specification.id !== undefined) {
            this.subscribeToSaveResponse(
                this.specificationService.update(this.specification));
        } else {
            this.subscribeToSaveResponse(
                this.specificationService.create(this.specification));
        }
    }

    private subscribeToSaveResponse(result: Observable<Specification>) {
        result.subscribe((res: Specification) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Specification) {
        this.eventManager.broadcast({ name: 'specificationListModification', content: 'OK'});
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

    trackDomainById(index: number, item: Domain) {
        return item.id;
    }

    trackMetaDataById(index: number, item: MetaData) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-specification-popup',
    template: ''
})
export class SpecificationPopupComponent implements OnInit, OnDestroy {

    modalRef: NgbModalRef;
    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private specificationPopupService: SpecificationPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.modalRef = this.specificationPopupService
                    .open(SpecificationDialogComponent, params['id']);
            } else {
                this.modalRef = this.specificationPopupService
                    .open(SpecificationDialogComponent);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
