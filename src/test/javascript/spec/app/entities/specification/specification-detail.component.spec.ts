/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TestbedTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { SpecificationDetailComponent } from '../../../../../../main/webapp/app/entities/specification/specification-detail.component';
import { SpecificationService } from '../../../../../../main/webapp/app/entities/specification/specification.service';
import { Specification } from '../../../../../../main/webapp/app/entities/specification/specification.model';

describe('Component Tests', () => {

    describe('Specification Management Detail Component', () => {
        let comp: SpecificationDetailComponent;
        let fixture: ComponentFixture<SpecificationDetailComponent>;
        let service: SpecificationService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TestbedTestModule],
                declarations: [SpecificationDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    SpecificationService,
                    JhiEventManager
                ]
            }).overrideTemplate(SpecificationDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(SpecificationDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(SpecificationService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Specification(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.specification).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
