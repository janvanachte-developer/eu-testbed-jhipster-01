/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TestbedTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MetaDataDetailComponent } from '../../../../../../main/webapp/app/entities/meta-data/meta-data-detail.component';
import { MetaDataService } from '../../../../../../main/webapp/app/entities/meta-data/meta-data.service';
import { MetaData } from '../../../../../../main/webapp/app/entities/meta-data/meta-data.model';

describe('Component Tests', () => {

    describe('MetaData Management Detail Component', () => {
        let comp: MetaDataDetailComponent;
        let fixture: ComponentFixture<MetaDataDetailComponent>;
        let service: MetaDataService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TestbedTestModule],
                declarations: [MetaDataDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    MetaDataService,
                    JhiEventManager
                ]
            }).overrideTemplate(MetaDataDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(MetaDataDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(MetaDataService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new MetaData(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.metaData).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
